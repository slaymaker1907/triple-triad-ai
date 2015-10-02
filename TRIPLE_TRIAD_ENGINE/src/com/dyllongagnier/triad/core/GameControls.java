package com.dyllongagnier.triad.core;

import java.util.ArrayList;
import java.util.List;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.MoveValidator;

public class GameControls
{
	private final Player currentPlayer;

	/**
	 * This is the mostly immutable board state. However, agents are trusted to NOT deploy
	 * any cards from hand unless it is explicitly allowed by the UndeployedCard in question otherwise
	 * undefined behavior will occur.
	 */
	public final BoardState currentTurn;
	
	private BoardState nextTurn;
	public final MoveValidator moveValidator;
	public final boolean isOrder;

	/**
	 * Constructs a new GameControls from the currentTurn as well as the
	 * currentPlayer who will take this turn.
	 * 
	 * @param currentTurn
	 *            The current state of the board for this turn.
	 * @param currentPlayer
	 *            The player taking this turn.
	 */
	public GameControls(BoardState currentTurn, Player currentPlayer,
			MoveValidator moveValidator, boolean isOrder)
	{
		this.currentTurn = currentTurn;
		this.currentPlayer = currentPlayer;
		this.nextTurn = null;
		this.moveValidator = moveValidator;
		this.isOrder = isOrder;
	}

	/**
	 * Plays a given card from the currentPlayer's hand. This method will throw
	 * an exception if the input card is not in the currentPlayer's hand or if
	 * the row/col specified is occupied or out of range. This method will also
	 * throw an exception if this method has already been called.
	 * 
	 * @param card
	 *            The card to play.
	 * @param row
	 *            The row to play the card at.
	 * @param col
	 *            The col to play the card at.
	 */
	public void playCard(UndeployedCard card, int row, int col)
	{
		// These can may be replaced with assert statements to be compiled away.
		if (this.nextTurn != null)
			throw new IllegalArgumentException(
					"This turn is already complete and can not be changed.");
		if (this.moveValidator.apply(this.currentTurn, card,
				this.currentPlayer, row, col))
			throw new IllegalArgumentException();

		this.nextTurn = this.currentTurn.playCard(this.currentPlayer, card,
				row, col);
	}

	/**
	 * Only return a clone since BoardState isn't truly immutable.
	 * 
	 * @return A clone of the current turn.
	 */
	public BoardState getCopyOfBoard()
	{
		// It may be prudent to add some methods to access board data without
		// cloning the board for certain AIs.
		return this.currentTurn.clone();
	}

	/**
	 * This method constructs a BoardState from this object. This method will
	 * throw an exception if playCard has not yet been called.
	 * 
	 * @return The nextTurn BoardState.
	 */
	public BoardState getNextTurn()
	{
		assert this.nextTurn != null;
		return this.nextTurn;
	}
	
	/**
	 * This method retrieves all possible valid moves.
	 * @return A list with all possible valid moves.
	 */
	public List<PossibleMove> getValidMoves()
	{
		ArrayList<PossibleMove> result = new ArrayList<>();
		if (isOrder)
		{
			for(int row = 0; row < 3; row++)
				for(int col = 0; col < 3; col++)
				{
					UndeployedCard toPlay = this.currentTurn.getFirstCardInHand(this.currentPlayer);
					if (this.moveValidator.apply(this.currentTurn, toPlay, this.currentPlayer, row, col))
						result.add(new PossibleMove(toPlay, row, col));
				}
		}
		else
		{
			for(UndeployedCard card : this.currentTurn.getHand(this.currentPlayer))
				for(int row = 0; row < 3; row++)
					for(int col = 0; col < 3; col++)
						if (this.moveValidator.apply(this.currentTurn, card, this.currentPlayer, row, col))
							result.add(new PossibleMove(card, row, col));
		}
		
		return result;
	}
	
	public static class PossibleMove
	{
		public final UndeployedCard toPlay;
		public final int row, col;
		
		public PossibleMove(UndeployedCard toPlay, int row, int col)
		{
			this.toPlay = toPlay;
			this.row = row;
			this.col = col;
		}
	}
}
