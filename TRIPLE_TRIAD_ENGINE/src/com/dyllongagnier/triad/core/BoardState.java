package com.dyllongagnier.triad.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.AscensionTransform;
import com.dyllongagnier.triad.core.functions.CardPlayFunction;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;
import com.dyllongagnier.triad.core.functions.MoveValidator;

/**
 * This class represents a (mostly) immutable BoardState for triple triad. This
 * is guaranteed to provide enough state for the Basic game, however, more
 * complex games may need to extend this class. Player hands may not be
 * completely immutable due to IO and thus need to be cloned if copying.
 */
public class BoardState
{
	private final EnumMap<Player, Set<UndeployedCard>> playerHands;
	public final Field playedCards;
	private final Player firstPlayer;

	/**
	 * Constructs a new BoardState. Unless doing something particular, the
	 * Builder should be used to construct this object.
	 * 
	 * @param playerHands
	 *            The map to use for hands. However, this constructor will first
	 *            ensure that the sets are unmodifiable. Also, the Player.NONE
	 *            type is ignored.
	 * @param playedCards
	 *            The currently played cards on the board. This constructor
	 *            directly stores this array, so any modifications to the input
	 *            array will be reflected in this state.
	 */
	protected BoardState(
			EnumMap<Player, Set<UndeployedCard>> playerHands,
			Field playedCards, Player firstPlayer)
	{
		this.playerHands = playerHands;
		this.playedCards = playedCards;
		this.firstPlayer = firstPlayer;
	}

	@Override
	public BoardState clone()
	{
		return this;
	}

	/**
	 * This method returns the hand of the input player.
	 * 
	 * @param player
	 *            The player whose hand will be returned. Must be non-null and
	 *            not Player.NONE.
	 * @return An unmodifiable list view of the hand.
	 */
	public Set<UndeployedCard> getHand(Player player)
	{
		assert player != Player.NONE;
		assert player != null;
		return this.playerHands.get(player);
	}

	/**
	 * This method returns whether this BoardState is completed (9 cards are
	 * played).
	 * 
	 * @return True if there are 9 played cards.
	 */
	public boolean gameComplete()
	{
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				if (!this.playedCards.isCardInPos(row, col))
					return false;
		return true;
	}

	/**
	 * This method returns the current winner. Game should be complete before
	 * this method is called.
	 * 
	 * @return The winner of the game.
	 */
	public Player getWinner()
	{
		assert this.gameComplete();
		
		Function<Player, Integer> playerPoints = this.getPlayerScore();
		int selfPoints, opponentPoints;
		selfPoints = playerPoints.apply(Player.BLUE);
		opponentPoints = playerPoints.apply(Player.RED);
		
		if (selfPoints > opponentPoints)
			return Player.BLUE;
		else if (opponentPoints > selfPoints)
			return Player.RED;
		else
			return Player.NONE;
	}

	/**
	 * This method returns the first card in the input player's hand.
	 * 
	 * @param player
	 *            The player to get the first card of. This player should be
	 *            non-null and not Player.NONE.
	 * @return The first card in the input player's hand.
	 */
	public UndeployedCard getFirstCardInHand(Player player)
	{
		assert player != null;
		assert player != Player.NONE;
		return ((SortedSet<UndeployedCard>)this.playerHands.get(player)).first();
	}

	/**
	 * This method plays the input card following all rules.
	 * 
	 * @param player
	 *            The player of the card.
	 * @param card
	 *            The card to play.
	 * @param row
	 *            The row to play the card at.
	 * @param col
	 *            The column to play the card at.
	 * @return The resulting board state.
	 */
	public BoardState playCard(Player player, UndeployedCard card, int row,
			int col)
	{
		assert player != Player.NONE;
		assert player != null;
		assert card != null;
		assert this.playerHands.get(player).contains(card);

		DeployedCard cardToPlay = new DeployedCard(card.deploy(), row, col);
		assert cardToPlay.card.holdingPlayer == player;
		Field newField = this.playedCards.playCard(cardToPlay);
		EnumMap<Player, Set<UndeployedCard>> newHands = new EnumMap<>(
				Player.class);
		
		// Do a check for Order.
		Set<UndeployedCard> oldChangedCards = this.getHand(player);
		Set<UndeployedCard> newChangedCards;
		if (oldChangedCards instanceof TreeSet<?>)
			newChangedCards = new TreeSet<UndeployedCard>(oldChangedCards);
		else
			newChangedCards = new HashSet<UndeployedCard>(oldChangedCards);
		
		newChangedCards.remove(card);
		newHands.put(player, newChangedCards);
		newHands.put(player.swapPlayer(), this.getHand(player.swapPlayer()));
		return new BoardState(newHands, newField, this.firstPlayer);
	}

	/**
	 * This method returns a setup for a new game with UndeployedCards
	 * corresponding to each player. This method should only be called after the
	 * end of the game. This should also only be called in the case of a draw.
	 * 
	 * @return A function that returns the cards for each player.
	 */
	public Function<Player, UndeployedCard[]> getCardsUnderPlayers()
	{
		Function<Player, ArrayList<UndeployedCard>> boardFunc = this.playedCards
				.getCardsUnderPlayers();
		ArrayList<UndeployedCard> selfCards = boardFunc.apply(Player.BLUE);
		ArrayList<UndeployedCard> opponentCards = boardFunc
				.apply(Player.RED);
		
		opponentCards.addAll(this.getHand(Player.RED));
		selfCards.addAll(this.getHand(Player.BLUE));

		return (player) ->
		{
			switch (player)
			{
				case BLUE:
					return selfCards.toArray(new UndeployedCard[selfCards
							.size()]);
				case RED:
					return opponentCards
							.toArray(new UndeployedCard[opponentCards.size()]);
				default:
					assert false;
					return new UndeployedCard[0];
			}
		};
	}
	
	public Function<Player, Integer> getPlayerScore()
	{
		Function<Player, Integer> subFunc = this.playedCards.getVictoryPoints();
		return (player) ->
		{
			if (player != this.firstPlayer)
				return subFunc.apply(player) + 1;
			else
				return subFunc.apply(player);
		};
	}

	/**
	 * This class is used to construct BoardStates and is mutable.
	 */
	public static class Builder
	{
		private final EnumMap<Player, SortedSet<UndeployedCard>> playerHands;
		private MoveValidator validator;
		private AscensionTransform ascensionTransform;
		private boolean isOrder;
		private AscensionRule ascensionRule;

		public boolean isSuddenDeath, isReverse, isFallenAce, isPlus, isSame,
				isCombo;

		/**
		 * Constructs a new state with no cards in hands and no played cards.
		 * Rules default to no special rules.
		 */
		public Builder()
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.BLUE, new TreeSet<>());
			this.playerHands.put(Player.RED, new TreeSet<>());
			this.validator = MoveValidator::normalValidator;
			this.ascensionTransform = AscensionTransform::noAscension;
			this.isOrder = false;
			this.ascensionRule = AscensionRule.NONE;
			this.isSuddenDeath = this.isReverse = this.isPlus = this.isSame = this.isCombo = false;
		}
		
		public Builder(Builder other)
		{
			this();
			this.isCombo = other.isCombo;
			this.isFallenAce = other.isFallenAce;
			this.isPlus = other.isPlus;
			this.isReverse = other.isReverse;
			this.isSame = other.isSame;
			this.isSuddenDeath = other.isSuddenDeath;
			this.setAscensionTransform(this.getAscensionRule());
			this.setHand(Player.BLUE, other.copyPlayerHand(Player.BLUE));
			this.setHand(Player.RED, other.copyPlayerHand(Player.RED));
			this.setIsOrder(other.isOrder);
		}
		
		private UndeployedCard[] copyPlayerHand(Player player)
		{
			SortedSet<UndeployedCard> cards = this.playerHands.get(player);
			UndeployedCard[] result = new UndeployedCard[cards.size()];
			int i = 0;
			for(UndeployedCard card : cards)
				result[i++] = card;
			return result;
		}

		/**
		 * This method sets isOrder. If isOrder is true, then an extra check is
		 * performed during the game to make sure that the only valid move is
		 * the first card in a player's hand.
		 * 
		 * @param isOrder
		 * @return This object.
		 */
		public Builder setIsOrder(boolean isOrder)
		{
			if (isOrder)
			{
				this.validator = MoveValidator::partialOrderValidator;
			} else
			{
				this.validator = MoveValidator::normalValidator;
			}

			this.isOrder = isOrder;

			return this;
		}

		/**
		 * This method returns whether this object is using the isOrder rule.
		 * 
		 * @return
		 */
		public boolean getIsOrder()
		{
			return this.isOrder;
		}

		/**
		 * This method retrieves the move validator that should be used to
		 * verify moves in this game.
		 * 
		 * @return
		 */
		public MoveValidator getMoveValidator()
		{
			return this.validator;
		}

		/**
		 * This method sets the ascension rule to use at runtime.
		 * 
		 * @param rule
		 *            The ascension rule to use (can be nothing).
		 * @return This object.
		 */
		public Builder setAscensionTransform(AscensionRule rule)
		{
			switch (rule)
			{
				case NONE:
					this.ascensionTransform = AscensionTransform::noAscension;
					break;
				case NORMAL:
					this.ascensionTransform = AscensionTransform::ascension;
					break;
				case DESCENSION:
					this.ascensionTransform = AscensionTransform::descension;
					break;
				default:
					throw new IllegalArgumentException();
			}

			this.ascensionRule = rule;
			return this;
		}

		/**
		 * This method retrieves the ascension rule to be used with this
		 * builder.
		 * 
		 * @return An ascension rule (none, ascension, descension).
		 */
		public AscensionRule getAscensionRule()
		{
			return this.ascensionRule;
		}

		/**
		 * This method sets a given player's hand.
		 * 
		 * @param player
		 *            The player to set the hand of. Must not be Player.NONE
		 * @param cards
		 *            The cards to set the hand as.
		 * @return This object.
		 */
		public Builder setHand(Player player, UndeployedCard... cards)
		{
			assert player != null;
			assert cards != null;
			assert player != Player.NONE;
			assert assertAllCardsNonNull(cards);
			
			this.playerHands.put(player, new TreeSet<>(Arrays.asList(cards)));
			return this;
		}
		
		private static boolean assertAllCardsNonNull(UndeployedCard[] cards)
		{
			for(UndeployedCard card : cards)
			{
				if (card == null)
					return false;
			}
			
			return true;
		}

		/**
		 * This method constructs a BoardState given this object.
		 * 
		 * @return A new BoardState using the input parameters to this object.
		 */
		public BoardState build(Player firstPlayer)
		{
			DeployedCardComparator cardComparator = Builder.getComparator(
					isReverse, isFallenAce);
			CardPlayFunction playFunc = Builder.getPlayFunction(isPlus, isSame,
					isCombo);
			Field fieldToUse;
			switch (this.ascensionRule)
			{
				case NORMAL:
				case DESCENSION:
					fieldToUse = new AscensionField(cardComparator,
							this.ascensionTransform, playFunc);
					break;
				default:
					fieldToUse = new Field(cardComparator, playFunc);
			}
			
			EnumMap<Player, Set<UndeployedCard>> playerHands = new EnumMap<>(Player.class);
			if (this.isOrder)
			{
				playerHands.put(Player.BLUE, new TreeSet<UndeployedCard>());
				playerHands.put(Player.RED, new TreeSet<UndeployedCard>());
			}
			else
			{
				playerHands.put(Player.BLUE, new HashSet<UndeployedCard>());
				playerHands.put(Player.RED, new HashSet<UndeployedCard>());
			}
			playerHands.get(Player.BLUE).addAll(this.playerHands.get(Player.BLUE));
			playerHands.get(Player.RED).addAll(this.playerHands.get(Player.RED));

			return new BoardState(playerHands, fieldToUse, firstPlayer);
		}

		/**
		 * This method returns the comparator associated with this rule set.
		 * 
		 * @boolean isReverse Indicates that the Reverse rule is in place.
		 * @boolean isFallenAce Indicates that the Fallen Ace rule is in place.
		 * @return A comparator associated with the input rules.
		 */
		private static DeployedCardComparator getComparator(boolean isReverse,
				boolean isFallenAce)
		{
			if (isReverse && isFallenAce)
				return DeployedCardComparator::fallenAceReverseCompare;
			else if (isReverse)
				return DeployedCardComparator::reverseCompare;
			else if (isFallenAce)
				return DeployedCardComparator::fallenAceCompare;
			else
				return DeployedCardComparator::regularCompare;
		}

		/**
		 * This method gets the relevant play function for the input options.
		 * 
		 * @param isPlus
		 *            Indicates the Plus rule.
		 * @param isSame
		 *            Indicates the Same rule.
		 * @param isCombo
		 *            Indicates the Combo rule. This can only be selected with
		 *            isPlus or isSame.
		 * @return The appropriate play function.
		 */
		private static CardPlayFunction getPlayFunction(boolean isPlus,
				boolean isSame, boolean isCombo)
		{
			if (!isPlus && !isSame && !isCombo)
				return CardPlayFunction::basicCapture;
			else if (isPlus && !isSame && !isCombo)
				return CardPlayFunction::plusCapture;
			else if (!isPlus && isSame && !isCombo)
				return CardPlayFunction::sameCapture;
			else if (isPlus && isSame && !isCombo)
				return CardPlayFunction::samePlus;
			else if (isPlus && !isSame && isCombo)
				return CardPlayFunction::plusCombo;
			else if (!isPlus && isSame && isCombo)
				return CardPlayFunction::sameCombo;
			else if (isPlus && isSame && isCombo)
				return CardPlayFunction::samePlusCombo;
			else
				throw new IllegalArgumentException(
						"Must select isPlus and/or isSame with isCombo.");
		}
	}
}
