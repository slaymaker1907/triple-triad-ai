package com.dyllongagnier.triad.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

/**
 * This class represents a (mostly) immutable BoardState for triple triad. This is
 * guaranteed to provide enough state for the Basic game, however, more complex
 * games may need to extend this class. Player hands may not be completely immutable due
 * to IO and thus need to be cloned if copying.
 */
public class BoardState
{
	private final EnumMap<Player, SortedSet<UndeployedCard>> playerHands = new EnumMap<>(
			Player.class);
	public final Field playedCards;

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
	protected BoardState(EnumMap<Player, SortedSet<UndeployedCard>> playerHands, Field playedCards)
	{
		SortedSet<UndeployedCard> opponentCards = Collections.unmodifiableSortedSet(playerHands
				.get(Player.OPPONENT));
		SortedSet<UndeployedCard> selfCards = Collections.unmodifiableSortedSet(playerHands
				.get(Player.SELF));
		this.playerHands.put(Player.OPPONENT, opponentCards);
		this.playerHands.put(Player.SELF, selfCards);
		this.playedCards = playedCards;
	}
	
	@Override
	public BoardState clone()
	{
		// Do a deep clone on the player hands to ensure immutability.
		EnumMap<Player, SortedSet<UndeployedCard>> newPlayerHands = new EnumMap<>(Player.class);
		SortedSet<UndeployedCard> selfHand = new TreeSet<>();
		for(UndeployedCard card : this.playerHands.get(Player.SELF))
			selfHand.add(card);
		SortedSet<UndeployedCard> opponentHand = new TreeSet<>();
		for(UndeployedCard card : this.playerHands.get(Player.OPPONENT))
			opponentHand.add(card);
		newPlayerHands.put(Player.SELF, selfHand);
		newPlayerHands.put(Player.OPPONENT, opponentHand);
		
		return new BoardState(newPlayerHands, this.playedCards);
	}

	/**
	 * This method returns the hand of the input player.
	 * 
	 * @param player
	 *            The player whose hand will be returned. Must be non-null and
	 *            not Player.NONE.
	 * @return An unmodifiable list view of the hand.
	 */
	public SortedSet<UndeployedCard> getHand(Player player)
	{
		assert player != Player.NONE;
		assert player == null;
		return this.playerHands.get(player);
	}
	
	/**
	 * This method returns whether this BoardState is completed (9 cards are played).
	 * @return True if there are 9 played cards.
	 */
	public boolean gameComplete()
	{
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 3; col++)
				if (!this.playedCards.isCardInPos(row, col))
					return false;
		return true;
	}
	
	/**
	 * This method returns the current winner. Game should be complete before this method is called.
	 * @return The winner of the game.
	 */
	public Player getWinner()
	{
		assert this.gameComplete();
		
		EnumMap<Player, Integer> holdingCount = new EnumMap<>(Player.class);
		holdingCount.put(Player.SELF, 0);
		holdingCount.put(Player.OPPONENT, 0);
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 3; col++)
			{
				holdingCount.compute(
						this.playedCards.getCard(row, col).card.holdingPlayer, (player, currentVal) -> currentVal + 1);
			}
		
		int playerCards = holdingCount.get(Player.SELF) + this.getHand(Player.SELF).size();
		int opponentCards = holdingCount.get(Player.OPPONENT) + this.getHand(Player.OPPONENT).size();
		assert playerCards + opponentCards == 10;
		if (playerCards > opponentCards)
			return Player.SELF;
		else if (opponentCards> playerCards)
			return Player.OPPONENT;
		else
			return Player.NONE;
	}
	
	/**
	 * This method returns the first card in the input player's hand.
	 * @param player The player to get the first card of. This player should be non-null and not Player.NONE.
	 * @return The first card in the input player's hand.
	 */
	public UndeployedCard getFirstCardInHand(Player player)
	{
		assert player != null;
		assert player != Player.NONE;
		return this.playerHands.get(player).first();
	}
	
	/**
	 * This method plays the input card following all rules.
	 * @param player The player of the card.
	 * @param card The card to play.
	 * @param row The row to play the card at.
	 * @param col The column to play the card at.
	 * @return The resulting board state.
	 */
	public BoardState playCard(Player player, UndeployedCard card, int row, int col)
	{
		assert player != Player.NONE;
		assert player != null;
		assert card != null;
		assert this.playerHands.get(player).contains(card);
		
		DeployedCard cardToPlay = new DeployedCard(card.deploy(), row, col);
		assert cardToPlay.card.holdingPlayer == player;
		Field newField = this.playedCards.playCard(cardToPlay);
		return new BoardState(this.playerHands, newField);
	}
	
	/**
	 * This method returns a setup for a new game with UndeployedCards corresponding to each player.
	 * This method should only be called after the end of the game. This should also only be called in the case of a draw.
	 * @return A function that returns the cards for each player.
	 */
	public Function<Player, UndeployedCard[]> getCardsUnderPlayers()
	{
		assert this.gameComplete();
		
		Function<Player, ArrayList<UndeployedCard>> boardFunc = this.playedCards.getCardsUnderPlayers();
		ArrayList<UndeployedCard> selfCards = boardFunc.apply(Player.SELF);
		ArrayList<UndeployedCard> opponentCards = boardFunc.apply(Player.OPPONENT);
		assert boardFunc.apply(Player.NONE).size() == 0;
		selfCards.addAll(this.getHand(Player.SELF));
		opponentCards.addAll(this.getHand(Player.OPPONENT));
		
		assert selfCards.size() == opponentCards.size();
				
		return (player) ->
		{
			switch(player)
			{
				case SELF:
					return selfCards.toArray(new UndeployedCard[selfCards.size()]);
				case OPPONENT:
					return opponentCards.toArray(new UndeployedCard[opponentCards.size()]);
				default:
					assert false;
					return new UndeployedCard[0];
			}
		};
	}
	
	/**
	 * This class is used to construct BoardStates and is mutable.
	 */
	public static class Builder
	{
		protected final EnumMap<Player, SortedSet<UndeployedCard>> playerHands;
		private final Field fieldToUse;

		/**
		 * Constructs a new state with no cards in hands and no played cards.
		 * @param ruleSet The rules to use for this game.
		 */
		public Builder(Rules ruleSet)
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF, new TreeSet<>());
			this.playerHands.put(Player.OPPONENT, new TreeSet<>());
			if (ruleSet.ascensionRule != Rules.AscensionRule.NONE)
				this.fieldToUse = new AscensionField(ruleSet.cardComparator, ruleSet.ascensionFunc, ruleSet.playFunc);
			else
				this.fieldToUse = new Field(ruleSet.cardComparator, ruleSet.playFunc);
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

			Set<UndeployedCard> playerCards = playerHands.get(player);
			playerCards.clear();
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}

		/**
		 * This method constructs a BoardState given this object.
		 * @return A new BoardState using the input parameters to this object.
		 */
		public BoardState build()
		{
			return new BoardState(this.playerHands, this.fieldToUse);
		}
	}
}
