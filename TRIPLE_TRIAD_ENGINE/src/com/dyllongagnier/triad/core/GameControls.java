package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

public class GameControls
{
	private final Player currentPlayer;
	
	public final BoardState currentTurn;
	private BoardState nextTurn;
	
	/**
	 * Constructs a new GameControls from the currentTurn as well as the currentPlayer who
	 * will take this turn.
	 * @param currentTurn The current state of the board for this turn.
	 * @param currentPlayer The player taking this turn.
	 */
	public GameControls(BoardState currentTurn, Player currentPlayer)
	{
		this.currentTurn = currentTurn;
		this.currentPlayer = currentPlayer;
		this.nextTurn = null;
	}
	
	/**
	 * Plays a given card from the currentPlayer's hand. This method will throw an exception if
	 * the input card is not in the currentPlayer's hand or if the row/col specified is occupied or out of
	 * range. This method will also throw an exception if this method has already been called.
	 * @param card The card to play.
	 * @param row The row to play the card at.
	 * @param col The col to play the card at.
	 */
	public void playCard(UndeployedCard card, int row, int col)
	{
		// These can may be replaced with assert statements to be compiled away.
		if (this.nextTurn == null)
			throw new IllegalArgumentException("This turn is already complete and can not be changed.");
		if (!this.currentTurn.getHand(this.currentPlayer).contains(card))
			throw new IllegalArgumentException("Attempted to play a card not in hand.");
		if (this.currentTurn.playedCards.isCardInPos(row, col))
			throw new IllegalArgumentException();
		
		this.nextTurn = this.currentTurn.playCard(this.currentPlayer, card, row, col);
	}
	
	/**
	 * This method constructs a BoardState from this object. This method will throw an exception if playCard has not yet been
	 * called.
	 * @return The nextTurn BoardState.
	 */
	public BoardState getNextTurn()
	{
		assert this.nextTurn != null;
		return this.nextTurn;
	}
}
