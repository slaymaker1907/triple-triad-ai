package com.dyllongagnier.triad.core;

/**
 * Implementing classes act as agents to play TripleTriad. When takeTurn is called, the BoardState will wait for the
 * called upon agent to make a valid move. Agents should implement clone.
 */
public interface GameAgent
{
	/**
	 * This method asks the agent to begin making their move.
	 * @param game The current state of the game.
	 */
	public void takeTurn(TriadGame game);
	
	/**
	 * This method makes a suitable copy of this agent that should be automated.
	 * @return A new automated agent roughly equivalent to this one.
	 */
	public GameAgent clone();
}
