package com.dyllongagnier.triad.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

/**
 * This class represents an immutable BoardState for triple triad. This is
 * guaranteed to provide enough state for the Basic game, however, more complex
 * games may need to extend this class.
 */
public class BoardState
{
	private final EnumMap<Player, Set<UndeployedCard>> playerHands = new EnumMap<>(
			Player.class);
	private final Card[][] playedCards;

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
	protected BoardState(EnumMap<Player, Set<UndeployedCard>> playerHands,
			Card[][] playedCards)
	{
		Set<UndeployedCard> opponentCards = Collections.unmodifiableSet(playerHands
				.get(Player.OPPONENT));
		Set<UndeployedCard> selfCards = Collections.unmodifiableSet(playerHands
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
	 * @return An unmodifiable set view of the hand.
	 */
	public Set<UndeployedCard> getHand(Player player)
	{
		if (player == Player.NONE)
			throw new IllegalArgumentException();
		if (player == null)
			throw new NullPointerException();
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
	public Card getPlayedCard(int row, int col)
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
		if (!this.gameComplete())
			throw new IllegalArgumentException("Game is not yet complete.");
		
		EnumMap<Player, Integer> holdingCount = new EnumMap<>(Player.class);
		holdingCount.put(Player.SELF, 0);
		holdingCount.put(Player.OPPONENT, 0);
		for(Card[] row : this.playedCards)
		{
			for(Card card : row)
				holdingCount.compute(card.holdingPlayer, (player, currentVal) -> currentVal + 1);
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
	 * This method compares the relevant attributes of the cards depending on their positions.
	 * @param primaryCard The primary card for comparison.
	 * @param otherCard The other card for comparison.
	 * @param primRow The row of the primary card.
	 * @param primCol The column of the primary card.
	 * @param oRow The row of the other card.
	 * @param oCol The column of the other card.
	 * @return True if the primary card's stat is > the other card's relevant stat.
	 */
	public static boolean compareCardStrengths(Card primaryCard,
			Card otherCard, int primRow, int primCol, int oRow, int oCol)
	{
		if (!isInBounds(primRow, primCol))
			throw new IllegalArgumentException();
		if (!isInBounds(oRow, oCol))
			throw new IllegalArgumentException();

		if (primRow > oRow)
			return primaryCard.north > otherCard.south;
		else if (primRow < oRow)
			return primaryCard.south > otherCard.north;
		else if (primCol > oCol)
			return primaryCard.west > otherCard.east;
		else if (primCol < oCol)
			return primaryCard.east > otherCard.west;
		return false;
	}

	/**
	 * This class is used to construct BoardStates and is mutable.
	 */
	public static class Builder
	{
		protected final EnumMap<Player, Set<UndeployedCard>> playerHands;
		protected final Card[][] playedCards = new Card[9][9];

		/**
		 * Constructs a new state with no cards in hands and no played cards.
		 */
		public Builder()
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF, new HashSet<>());
			this.playerHands.put(Player.OPPONENT, new HashSet<>());
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
					new HashSet<>(oldState.getHand(Player.SELF)));
			this.playerHands.put(Player.OPPONENT,
					new HashSet<>(oldState.getHand(Player.OPPONENT)));
			for (int row = 0; row < 3; row++)
				for (int col = 0; col < 3; col++)
					this.playedCards[row][col] = oldState.getPlayedCard(row,
							col);
		}

		/**
		 * This places a card and throws an exception if card[row][col] is
		 * already filled.
		 * 
		 * @param card
		 *            The card to place.
		 * @param row
		 *            The row to place the card at.
		 * @param col
		 *            The column to place the card at.
		 * @return This object.
		 */
		public Builder placeCardSafely(Card card, int row, int col)
		{
			if (playedCards[row][col] != null)
				throw new IllegalArgumentException("Row:" + row + " Col:" + col);
			playedCards[row][col] = card;
			return this;
		}

		/**
		 * This method flips the possession of the card a (row,col) from
		 * SELF->OPPONENT or vice versa.
		 * 
		 * @param row
		 *            The row to change the card possession of.
		 * @param col
		 *            The column to chagne the card possesion of.
		 * @return This object.
		 */
		public Builder changeCardPossession(int row, int col)
		{
			Card oldCard = playedCards[row][col];
			playedCards[row][col] = oldCard
					.setHoldingPlayer(oldCard.holdingPlayer.swapPlayer());
			return this;
		}

		/**
		 * This method plays a given card from a player's hand at (row,col)
		 * using the playCardSafely method. It will get the card by calling
		 * card.deploy().
		 * 
		 * @param player
		 *            The player to take the card from.
		 * @param card
		 *            The card to play.
		 * @param row
		 *            The row to play the card at.
		 * @param col
		 *            The column to play the card at.
		 * @return This card that deploy was called upon.
		 */
		public Card playCardFromHand(Player player, UndeployedCard card, int row,
				int col)
		{
			this.removeFromHand(player, card);
			Card actualCard = card.deploy();
			this.placeCardSafely(actualCard, row, col);
			return actualCard;
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
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();

			Set<UndeployedCard> playerCards = playerHands.get(player);
			playerCards.clear();
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}

		/**
		 * This method adds cards to a player's hand.
		 * 
		 * @param player
		 *            The player whose hand will be added to.
		 * @param cards
		 *            The cards to add to the player's hand.
		 * @return This object.
		 */
		public Builder addToHand(Player player, UndeployedCard... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();

			Set<UndeployedCard> playerCards = playerHands.get(player);
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
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();

			Set<UndeployedCard> playerCards = playerHands.get(player);
			for (UndeployedCard card : cards)
				if (!playerCards.contains(card))
					throw new IllegalArgumentException("Hand does not contain:"
							+ card);

			playerCards.removeAll(Arrays.asList(cards));
			return this;
		}

		public Builder playCardAndCapture(Player player, UndeployedCard undeployedCard, int row,
				int col)
		{
			Card card = this.playCardFromHand(player, undeployedCard, row, col);
			
			CardReplacementFunc tryToCapture = (otherCard, otherRow, otherCol) ->
			{
				if (BoardState.compareCardStrengths(card, otherCard, row, col, otherRow, otherCol))
					return otherCard.setHoldingPlayer(otherCard.holdingPlayer.swapPlayer());
				else
					return otherCard;
			};
			this.replaceNeighboringSquares(row, col, tryToCapture);
			
			return this;
		}

		public Builder replaceNeighboringSquares(int row, int col,
				CardReplacementFunc replacementFunc)
		{
			BiConsumer<Integer, Integer> checkNeighbor = (currentRow,
					currentCol) ->
			{
				if (BoardState.isInBounds(currentRow, currentCol))
					this.playedCards[currentRow][currentCol] = replacementFunc
							.replaceCard(
									this.playedCards[currentRow][currentCol],
									row, col);
			};
			checkNeighbor.accept(row + 1, col);
			checkNeighbor.accept(row - 1, col);
			checkNeighbor.accept(row, col + 1);
			checkNeighbor.accept(row, col - 1);
			return this;
		}

		/**
		 * This method constructs a BoardState given this object.
		 * 
		 * @return
		 */
		public BoardState build()
		{
			return new BoardState(this.playerHands, this.playedCards);
		}
	}
}
