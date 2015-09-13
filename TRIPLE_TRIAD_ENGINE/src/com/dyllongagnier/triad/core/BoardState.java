package com.dyllongagnier.triad.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

/**
 * This class represents an immutable BoardState for triple triad. This is guaranteed to provide
 * enough state for the Basic game, however, more complex games may need to extend this class.
 */
public class BoardState
{
	protected final EnumMap<Player, Set<Card>> playerHands = new EnumMap<>(Player.class);
	protected final Card[][] playedCards;
	
	/**
	 * Constructs a new BoardState. Unless doing something particular, the Builder should be used to construct this object.
	 * @param playerHands The map to use for hands. However, this constructor will first ensure that the
	 * sets are unmodifiable. Also, the Player.NONE type is ignored.
	 * @param playedCards The currently played cards on the board. This constructor directly stores this array,
	 * so any modifications to the input array will be reflected in this state.
	 */
	protected BoardState(EnumMap<Player, Set<Card>> playerHands, Card[][] playedCards)
	{
		Set<Card> opponentCards = Collections.unmodifiableSet(playerHands.get(Player.OPPONENT));
		Set<Card> selfCards = Collections.unmodifiableSet(playerHands.get(Player.SELF));
		this.playerHands.put(Player.OPPONENT, opponentCards);
		this.playerHands.put(Player.SELF, selfCards);
		this.playedCards = playedCards;
	}
	
	/**
	 * This method returns the hand of the input player.
	 * @param player The player whose hand will be returned. Must be non-null and not Player.NONE.
	 * @return An unmodifiable set view of the hand.
	 */
	public Set<Card> getHand(Player player)
	{
		if (player == Player.NONE)
			throw new IllegalArgumentException();
		if (player == null)
			throw new NullPointerException();
		return this.playerHands.get(player);
	}
	
	/**
	 * Gets the played card and row and col.
	 * @param row The row of the played card.
	 * @param col The column of the played card.
	 * @return null if the slot is empty or a Card if there is a card played there.
	 */
	public Card getPlayedCard(int row, int col)
	{
		// The played card array can either be null if no card or non-null if not empty.
		return playedCards[row][col];
	}
	
	/**
	 * This method returns if the specified spot on the board is currently empty.
	 * @param row The row of the specified spot.
	 * @param col The column of the specified spot.
	 * @return True if and only if the specified spot is empty.
	 */
	public boolean spotEmpty(int row, int col)
	{
		return this.getPlayedCard(row, col) == null;
	}
	
	/**
	 * This class is usedto construct BoardStates and is mutable.
	 */
	public static class Builder
	{
		protected final EnumMap<Player, Set<Card>> playerHands;
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
		 * This class constructs a Builder such that if Builder was called, it would be identical to oldState.
		 * @param oldState The state to base this builder off of.
		 */
		public Builder(BoardState oldState)
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF, new HashSet<>(oldState.getHand(Player.SELF)));
			this.playerHands.put(Player.OPPONENT, new HashSet<>(oldState.getHand(Player.OPPONENT)));
			for(int row = 0; row < 9; row++)
				for(int col = 0; col < 9; col++)
					this.playedCards[row][col] = oldState.getPlayedCard(row, col);
		}
		
		/**
		 * This places a card and throws an exception if card[row][col] is already filled.
		 * @param card The card to place.
		 * @param row The row to place the card at.
		 * @param col The column to place the card at.
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
		 * This method flips the possession of the card a (row,col) from SELF->OPPONENT
		 * or vice versa.
		 * @param row The row to change the card possession of.
		 * @param col The column to chagne the card possesion of.
		 * @return This object.
		 */
		public Builder changeCardPossession(int row, int col)
		{
			Player oldPlayer = playedCards[row][col].holdingPlayer;
			switch(oldPlayer)
			{
				case SELF:
					playedCards[row][col] = playedCards[row][col].setHoldingPlayer(Player.OPPONENT);
					break;
				case OPPONENT:
					playedCards[row][col] = playedCards[row][col].setHoldingPlayer(Player.SELF);
					break;
				default:
					throw new IllegalArgumentException("Card possesor was not self or opponent for a played card.");
			}
			
			return this;
		}
		
		/**
		 * This method plays a given card from a player's hand at (row,col) using the playCardSafely method.
		 * @param player The player to take the card from.
		 * @param card The card to play.
		 * @param row The row to play the card at.
		 * @param col The column to play the card at.
		 * @return This object.
		 */
		public Builder playCardFromHand(Player player, Card card, int row, int col)
		{
			return this.removeFromHand(player, card).placeCardSafely(card, row, col);
		}
		
		/**
		 * This method sets a given player's hand.
		 * @param player The player to set the hand of. Must not be Player.NONE
		 * @param cards The cards to set the hand as.
		 * @return This object.
		 */
		public Builder setHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			playerCards.clear();
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}
		
		/**
		 * This method adds cards to a player's hand.
		 * @param player The player whose hand will be added to.
		 * @param cards The cards to add to the player's hand.
		 * @return This object.
		 */
		public Builder addToHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}
		
		/**
		 * This method removes the input cards from a player's hand.
		 * @param player The player whose hand will have cards removed from.
		 * @param cards The cards to remove from the player's hand.
		 * @return This object.
		 */
		public Builder removeFromHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			for(Card card : cards)
				if (!playerCards.contains(card))
					throw new IllegalArgumentException("Hand does not contain:" + card);
			
			playerCards.removeAll(Arrays.asList(cards));
			return this;
		}
		
		/**
		 * This method constructs a BoardState given this object.
		 * @return
		 */
		public BoardState build()
		{
			return new BoardState(this.playerHands, this.playedCards);
		}
	}
}
