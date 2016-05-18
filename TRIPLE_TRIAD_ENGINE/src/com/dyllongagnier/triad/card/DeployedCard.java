package com.dyllongagnier.triad.card;

/**
 * This class represents a card that has been played on the board.
 */
public class DeployedCard
{
	public final Card card;
	public final int row, col;

	/**
	 * This enum represents on of the four cardinal directions.
	 */
	public static enum Direction
	{
		NORTH, SOUTH, EAST, WEST
	}

	/**
	 * This method creates a new card with the specified parameters. It will
	 * deploy the UndeployedCard.
	 * 
	 * @param card
	 *            The card to play.
	 * @param row
	 *            The row of the card.
	 * @param col
	 *            The column of the card.
	 */
	public DeployedCard(UndeployedCard card, int row, int col)
	{
		assert row >= 0 && row < 3 && col >= 0 && col < 3;
		assert card != null;
		this.card = card.deploy();
		this.row = row;
		this.col = col;
	}

	/**
	 * This method returns the direction of the other card in relation to this
	 * card.
	 * 
	 * @param other
	 *            The other card to compare. It should be one square away either
	 *            left/right or up/down.
	 * @return The direction of the other card in relation to this card.
	 */
	public Direction getDirectionOfOther(DeployedCard other)
	{
		assert (Math.abs(other.row - this.row) == 1 && other.col == this.col)
				|| (Math.abs(other.col - this.col) == 1 && other.row == this.row);
		if (other.row > this.row)
			return Direction.SOUTH;
		else if (other.row < this.row)
			return Direction.NORTH;
		else if (other.col > this.col)
			return Direction.EAST;
		else
			return Direction.WEST;
	}

	/**
	 * This method sets the player of the deployed card.
	 * 
	 * @param player
	 *            The player to set the owner of.
	 * @return The new DeployedCard.
	 */
	public DeployedCard setPlayer(Player player)
	{
		if (player != this.card.holdingPlayer)
		{
			Card newCard = this.card.setHoldingPlayer(player);
			return new DeployedCard(newCard, row, col);
		} else
			return this;
	}

	/**
	 * This method returns true if other is an adjacent square to this card.
	 * 
	 * @param other
	 *            The potentially adjacent card to this one.
	 * @return True if the other card is north, east, south, or west of this
	 *         card.
	 */
	public boolean cardAdjacent(DeployedCard other)
	{
		return (Math.abs(other.row - this.row) == 1 && other.col == this.col)
				|| (Math.abs(this.col - other.col) == 1 && this.row == other.row);
	}

	@Override
	public int hashCode()
	{
		return this.card.hashCode() ^ (this.row * 31) ^ (this.col * 17);
	}

	@Override
	public boolean equals(Object o)
	{
		try
		{
			DeployedCard other = (DeployedCard) o;
			return this.card.equals(other.card) && this.row == other.row
					&& this.col == other.col;
		} catch (Exception e)
		{
			assert false;
			return false;
		}
	}
	
	public DeployedCard increaseStats(int amt)
	{
		return new DeployedCard(this.card.increaseAllStats(amt), this.row, this.col);
	}
}
