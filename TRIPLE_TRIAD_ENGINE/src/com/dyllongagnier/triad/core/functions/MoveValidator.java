package com.dyllongagnier.triad.core.functions;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.BoardState;

/**
 * This interface is for a function that computes whether a move is legal or
 * not.
 */
@FunctionalInterface
public interface MoveValidator
{
	/**
	 * This function returns true if it legal to play toPlay at (row,col).
	 * 
	 * @param currentState
	 *            The current state of the game.
	 * @param toPlay
	 *            The card being played.
	 * @param player
	 *            The player playing toPlay.
	 * @param row
	 *            The row the card is played at.
	 * @param col
	 *            The column the card is played at.
	 * @return True if toPlay can be played at (row,col).
	 */
	public boolean apply(BoardState currentState, UndeployedCard toPlay,
			Player player, int row, int col);

	/**
	 * This function returns true if it legal to play toPlay at (row,col) under
	 * most game rules.
	 * 
	 * @param currentState
	 *            The current state of the game.
	 * @param toPlay
	 *            The card being played.
	 * @param player
	 *            The player playing toPlay.
	 * @param row
	 *            The row the card is played at.
	 * @param col
	 *            The column the card is played at.
	 * @return True if toPlay can be played at (row,col).
	 */
	public static boolean normalValidator(BoardState currentState,
			UndeployedCard toPlay, Player player, int row, int col)
	{
		if (!currentState.getHand(player).contains(toPlay))
			return false;
		if (currentState.playedCards.isCardInPos(row, col))
			return false;
		return true;
	}

	/**
	 * This function returns true if it legal to play toPlay at (row,col) under
	 * the order rule.
	 * 
	 * @param currentState
	 *            The current state of the game.
	 * @param toPlay
	 *            The card being played.
	 * @param player
	 *            The player playing toPlay.
	 * @param row
	 *            The row the card is played at.
	 * @param col
	 *            The column the card is played at.
	 * @return True if toPlay can be played at (row,col).
	 */
	public static boolean orderValidator(BoardState currentState,
			UndeployedCard toPlay, Player player, int row, int col)
	{
		if (!currentState.getFirstCardInHand(player).equals(toPlay))
			return false;
		if (currentState.playedCards.isCardInPos(row, col))
			return false;
		return true;
	}
	 
	/**
	 * This function returns true if it legal to play toPlay at (row,col) under
	 * the order rule for the self player but does not limit the opponent player.
	 * 
	 * @param currentState
	 *            The current state of the game.
	 * @param toPlay
	 *            The card being played.
	 * @param player
	 *            The player playing toPlay.
	 * @param row
	 *            The row the card is played at.
	 * @param col
	 *            The column the card is played at.
	 * @return True if toPlay can be played at (row,col).
	 */
	public static boolean partialOrderValidator(BoardState currentState,
			UndeployedCard toPlay, Player player, int row, int col)
	{
		if (player == Player.BLUE && !currentState.getFirstCardInHand(player).equals(toPlay))
			return false;
		if (currentState.playedCards.isCardInPos(row, col))
			return false;
		return true;
	}
}
