package com.dyllongagnier.triad.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.DeployedCardComparators;

/**
 * This class represents an immutable BoardState for triple triad. This is
 * guaranteed to provide enough state for the Basic game, however, more complex
 * games may need to extend this class.
 */
public class BoardState
{
	private final EnumMap<Player, SortedSet<UndeployedCard>> playerHands = new EnumMap<>(
			Player.class);
	private final DeployedCard[][] playedCards;

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
	protected BoardState(EnumMap<Player, SortedSet<UndeployedCard>> playerHands,
			DeployedCard[][] playedCards)
	{
		SortedSet<UndeployedCard> opponentCards = Collections.unmodifiableSortedSet(playerHands
				.get(Player.OPPONENT));
		SortedSet<UndeployedCard> selfCards = Collections.unmodifiableSortedSet(playerHands
				.get(Player.SELF));
		this.playerHands.put(Player.OPPONENT, opponentCards);
		this.playerHands.put(Player.SELF, selfCards);
		this.playedCards = playedCards;
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
	 * Gets the played card and row and col.
	 * 
	 * @param row
	 *            The row of the played card.
	 * @param col
	 *            The column of the played card.
	 * @return null if the slot is empty or a Card if there is a card played
	 *         there.
	 */
	public DeployedCard getPlayedCard(int row, int col)
	{
		// The played card array can either be null if no card or non-null if
		// not empty.
		return playedCards[row][col];
	}

	/**
	 * This method returns if the specified spot on the board is currently
	 * empty.
	 * 
	 * @param row
	 *            The row of the specified spot.
	 * @param col
	 *            The column of the specified spot.
	 * @return True if and only if the specified spot is empty.
	 */
	public boolean spotEmpty(int row, int col)
	{
		return this.getPlayedCard(row, col) == null;
	}

	/**
	 * This method returns true if row and col are between 0 and 8.
	 * 
	 * @param row
	 *            The row to check.
	 * @param col
	 *            The column to check.
	 * @return True if and only if row and col are between 0 and 8 inclusive.
	 */
	public static boolean isInBounds(int row, int col)
	{
		return row >= 0 && row <= 8 && col >= 0 && col <= 0;
	}
	
	/**
	 * This method returns whether this BoardState is completed (9 cards are played).
	 * @return True if there are 9 played cards.
	 */
	public boolean gameComplete()
	{
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 3; col++)
				if (this.playedCards[row][col] == null)
					return false;
		return true;
	}
	
	public Player getWinner()
	{
		assert this.gameComplete();
		
		EnumMap<Player, Integer> holdingCount = new EnumMap<>(Player.class);
		holdingCount.put(Player.SELF, 0);
		holdingCount.put(Player.OPPONENT, 0);
		for(DeployedCard[] row : this.playedCards)
		{
			for(DeployedCard card : row)
				holdingCount.compute(card.card.holdingPlayer, (player, currentVal) -> currentVal + 1);
		}
		
		int playerCards = holdingCount.get(Player.SELF);
		int opponentCards = holdingCount.get(Player.OPPONENT);
		if (playerCards > opponentCards)
			return Player.SELF;
		else if (opponentCards> playerCards)
			return Player.OPPONENT;
		else
			return Player.NONE;
	}

	/**
	 * This class is used to construct BoardStates and is mutable.
	 */
	public static class Builder
	{
		protected final EnumMap<Player, SortedSet<UndeployedCard>> playerHands;
		protected final DeployedCard[][] playedCards = new DeployedCard[9][9];

		/**
		 * Constructs a new state with no cards in hands and no played cards.
		 */
		public Builder()
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF, new TreeSet<>());
			this.playerHands.put(Player.OPPONENT, new TreeSet<>());
		}

		/**
		 * This class constructs a Builder such that if Builder was called, it
		 * would be identical to oldState.
		 * 
		 * @param oldState
		 *            The state to base this builder off of.
		 */
		public Builder(BoardState oldState)
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF,
					new TreeSet<>(oldState.getHand(Player.SELF)));
			this.playerHands.put(Player.OPPONENT,
					new TreeSet<>(oldState.getHand(Player.OPPONENT)));
			for (int row = 0; row < 3; row++)
				for (int col = 0; col < 3; col++)
					this.playedCards[row][col] = oldState.getPlayedCard(row,
							col);
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
		 * This method removes the input cards from a player's hand.
		 * 
		 * @param player
		 *            The player whose hand will have cards removed from.
		 * @param cards
		 *            The cards to remove from the player's hand.
		 * @return This object.
		 */
		public Builder removeFromHand(Player player, UndeployedCard... cards)
		{
			assert player != null;
			assert cards != null;
			assert cards.length > 0;
			assert player != Player.NONE;

			Set<UndeployedCard> playerCards = playerHands.get(player);
			assert playerCards.containsAll(Arrays.asList(cards));

			playerCards.removeAll(Arrays.asList(cards));
			return this;
		}

		/**
		 * This method deploys the undeployedCard, plays that result on the board at the specified position,
		 * and then captures adjacent cards if necessary.
		 * @param player The player who owns undeployedCard in their hand.
		 * @param undeployedCard The card to play.
		 * @param row The row to play the card at.
		 * @param col The column to play the card at.s
		 * @return This object.
		 */
		public Builder playCardAndCapture(Player player, UndeployedCard undeployedCard, int row,
				int col)
		{
			assert playedCards[row][col] == null;
			
			this.removeFromHand(player, undeployedCard);
			DeployedCard playedCard = new DeployedCard(undeployedCard, row, col);
			this.playedCards[row][col] = playedCard;
			
			// TODO Add in other game modes here.
			Function<DeployedCard, DeployedCard> tryToCapture = (otherCard) ->
			{
				if (DeployedCardComparators.regularCompare(playedCard, otherCard))
					return otherCard.swapPlayer();
				else
					return otherCard;
			};
			this.replaceNeighboringSquares(row, col, tryToCapture);
			
			return this;
		}

		/**
		 * This method uses the input function to replace cards neighboring the row, col square
		 * with new cards.
		 * @param row The row to check adjacent squares of.
		 * @param col The column to check adjacent squares of.
		 * @param replacementFunc The function to use for replacing these cards.
		 * @return This object.
		 */
		private Builder replaceNeighboringSquares(int row, int col,
				Function<DeployedCard, DeployedCard> replacementFunc)
		{
			BiConsumer<Integer, Integer> checkNeighbor = (currentRow,
					currentCol) ->
			{
				if (BoardState.isInBounds(currentRow, currentCol))
					this.playedCards[currentRow][currentCol] =
						replacementFunc.apply(this.playedCards[currentRow][currentCol]);
			};
			checkNeighbor.accept(row + 1, col);
			checkNeighbor.accept(row - 1, col);
			checkNeighbor.accept(row, col + 1);
			checkNeighbor.accept(row, col - 1);
			return this;
		}

		/**
		 * This method constructs a BoardState given this object.
		 * @return A new BoardState using the input parameters to this object.
		 */
		public BoardState build()
		{
			return new BoardState(this.playerHands, this.playedCards);
		}
	}
}
