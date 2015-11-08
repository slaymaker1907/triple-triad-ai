package com.dyllongagnier.triad.card;

import java.util.Random;

public enum Player
{
	BLUE, RED, NONE;

	/**
	 * This method swaps SELF->OPPONENT or vice versa. This method will throw an
	 * exception if this object is Player.NONE
	 * 
	 * @return The other player.
	 */
	public Player swapPlayer()
	{
		switch (this)
		{
			case BLUE:
				return RED;
			case RED:
				return BLUE;
			default:
				throw new IllegalArgumentException();
		}
	}

	private static final Random gen = new Random();

	/**
	 * This method returns a random player (either SELF or OPPONENT).
	 * 
	 * @return A random player.
	 */
	public static Player getRandomPlayer()
	{
		int roll = gen.nextInt(2);
		if (roll == 0)
			return Player.BLUE;
		else
			return Player.RED;
	}
}
