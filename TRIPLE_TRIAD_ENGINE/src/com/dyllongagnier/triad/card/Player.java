package com.dyllongagnier.triad.card;

public enum Player
{
	SELF, OPPONENT, NONE;
	
	/**
	 * This method swaps SELF->OPPONENT or vice versa. This method will throw
	 * an exception if this object is Player.NONE
	 * @return The other player.
	 */
	public Player swapPlayer()
	{
		switch(this)
		{
			case SELF:
				return OPPONENT;
			case OPPONENT:
				return SELF;
			default:
				throw new IllegalArgumentException();
		}
	}
}
