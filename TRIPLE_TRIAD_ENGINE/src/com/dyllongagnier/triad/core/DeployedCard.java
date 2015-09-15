package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.UndeployedCard;

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
	 * This method creates a new card with the specified parameters. It will deploy
	 * the UndeployedCard.
	 * @param card The card to play.
	 * @param row The row of the card.
	 * @param col The column of the card.
	 */
	public DeployedCard(UndeployedCard card, int row, int col)
	{
		assert BoardState.isInBounds(row, col);
		assert card != null;
		this.card = card.deploy();
		this.row = row;
		this.col = col;
	}
	
	/**
	 * This method returns the direction of the other card in relation to this card.
	 * @param other The other card to compare. It should be one square away either left/right or
	 * up/down.
	 * @return The direction of the other card in relation to this card.
	 */
	public Direction getDirectionOfOther(DeployedCard other)
	{
		assert (Math.abs(other.row - this.row) == 1 && other.col == this.col) ||
			(Math.abs(other.col - this.col) == 1 && other.row == this.row);
		if (other.row > this.row)
			return Direction.SOUTH;
		else if (other.row < this.row)
			return Direction.NORTH;
		else if (other.col > this.col)
			return Direction.EAST;
		else
			return Direction.WEST;
	}
	
	public DeployedCard swapPlayer()
	{
		Card newCard = this.card.setHoldingPlayer(this.card.holdingPlayer.swapPlayer());
		return new DeployedCard(newCard, row, col);
	}
}
