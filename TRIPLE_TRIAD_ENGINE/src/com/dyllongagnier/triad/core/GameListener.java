package com.dyllongagnier.triad.core;

/**
 * Implementing classes can be alerted to when any TriadGame changes (a card is
 * played).
 */
public interface GameListener
{
	/**
	 * This method will be called any time
	 * 
	 * @param changedGame
	 *            The game that has changed.
	 */
	public void gameChanged(TriadGame changedGame);

	/**
	 * This method will be called once the game is finished.
	 * 
	 * @param finalState
	 *            The final state of the listened to game.
	 */
	public void gameComplete(TriadGame finalState);
}
