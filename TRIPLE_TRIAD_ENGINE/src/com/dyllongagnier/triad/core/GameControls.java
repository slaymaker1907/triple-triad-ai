package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class GameControls
{
	private final BoardState.Builder nextTurn;
	private final Player currentPlayer;
	private boolean playedCard;
	
	public final BoardState currentTurn;
	
	/**
	 * Constructs a new GameControls from the currentTurn as well as the currentPlayer who
	 * will take this turn.
	 * @param currentTurn The current state of the board for this turn.
	 * @param currentPlayer The player taking this turn.
	 */
	public GameControls(BoardState currentTurn, Player currentPlayer)
	{
		this.currentTurn = currentTurn;
		this.nextTurn = new BoardState.Builder(currentTurn);
		this.currentPlayer = currentPlayer;
		this.playedCard = false;
	}
	
	/**
	 * Plays a given card from the currentPlayer's hand. This method will throw an exception if
	 * the input card is not in the currentPlayer's hand or if the row/col specified is occupied or out of
	 * range. This method will also throw an exception if this method has already been called.
	 * @param card The card to play.
	 * @param row The row to play the card at.
	 * @param col The col to play the card at.
	 */
	public void playCard(Card card, int row, int col)
	{
		// This should be an invariant for cards held in hand.
		assert card.holdingPlayer == currentPlayer;
		
		if (!this.currentTurn.getHand(this.currentPlayer).contains(card))
			throw new IllegalArgumentException("Attempted to play a card not in hand.");
		if (this.playedCard)
			throw new IllegalArgumentException("This turn is already complete and can not be changed.");
		
		this.nextTurn.playCardFromHand(this.currentPlayer, card, row, col);
		this.playedCard = true;
	}
	
	/**
	 * This method constructs a BoardState from this object. This method will throw an exception if playCard has not yet been
	 * called.
	 * @return The nextTurn BoardState.
	 */
	public BoardState getNextTurn()
	{
		if (!this.playedCard)
			throw new IllegalArgumentException();
		return this.nextTurn.build();
	}
}
