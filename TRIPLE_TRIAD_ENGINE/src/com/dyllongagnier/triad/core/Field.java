package com.dyllongagnier.triad.core;

import java.util.Arrays;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;

/**
 * This class represents a field of cards. This object is immutable.
 */
public class Field 
{
	private final DeployedCard[][] playedCards;
	private final DeployedCardComparator cardComparator;
	
	/**
	 * Creates an empty field that will use the input cardComparator.
	 * @param cardComparator The comparator to use for determining victories.
	 */
	public Field(DeployedCardComparator cardComparator)
	{
		this.playedCards = new DeployedCard[3][3];
		this.cardComparator = cardComparator;
	}
	
	/**
	 * This method creates a new field with the input cards and the input comparator.
	 * @param playedCards The cards of the field.
	 * @param cardComparator The comparator to use in comparing cards.
	 */
	protected Field(DeployedCard[][] playedCards, DeployedCardComparator cardComparator)
	{
		this.playedCards = playedCards;
		this.cardComparator = cardComparator;
	}
	
	/**
	 * This method returns the card at row/col. This will be null if there
	 * is no card played there.
	 * @param row The row of the card to retrieve.
	 * @param col The column of the card to retrieve.
	 * @return The card at row/col.
	 */
	public DeployedCard getCard(int row, int col)
	{
		return playedCards[row][col];
	}
	
	/**
	 * This method plays the card in question using the cardComparator to determine if cards swap sides.
	 * @param cardToPlay The card that will be played.
	 * @return A new field with the reflected mutation.
	 */
	public Field playCard(DeployedCard cardToPlay)
	{
		assert cardToPlay != null;		
		int row = cardToPlay.row;
		int col = cardToPlay.col;
		assert !this.isCardInPos(row, col);
		
		DeployedCard[][] newPlayedCards = Arrays.copyOf(playedCards, playedCards.length);
		newPlayedCards[row][col] = cardToPlay;
		this.applyFunctionToPos(row + 1, col, newPlayedCards, cardToPlay);
		this.applyFunctionToPos(row - 1, col, newPlayedCards, cardToPlay);
		this.applyFunctionToPos(row, col + 1, newPlayedCards, cardToPlay);
		this.applyFunctionToPos(row, col - 1, newPlayedCards, cardToPlay);
		return new Field(newPlayedCards, this.cardComparator);
	}
	
	/**
	 * Determine if there is a card at row/col. row/col do not need to be in bounds.
	 * @param row The row to check.
	 * @param col The col to check.
	 * @return Whethere there is a card at row/col.
	 */
	public boolean isCardInPos(int row, int col)
	{
		return this.isInBounds(row, col) && this.playedCards[row][col] != null;
	}
	
	/**
	 * This method returns whether the input destination is in bounds (both >= 0 and < 3).
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether the row/column pair is in bounds.
	 */
	public boolean isInBounds(int row, int col)
	{
		return row >= 0 && row < 3 && col >= 0 && col < 3;
	}
	
	/**
	 * This method applies the cardComparator to the card at row/col if it exists in newPlayedCards
	 * and then stores that into the same position in newPlayedCards.
	 * @param row The row to transform.
	 * @param col The column to transform.
	 * @param newPlayedCards The array to mutate.
	 * @param cardToPlay The card that was just played.
	 */
	private void applyFunctionToPos(int row, int col, DeployedCard[][] newPlayedCards, DeployedCard cardToPlay)
	{
		assert (Math.abs(cardToPlay.row - row) == 1 && col == cardToPlay.col) || (Math.abs(col - cardToPlay.col) == 1
				&& row == cardToPlay.row);
		if (this.isCardInPos(row, col))
		{
			if(this.cardComparator.apply(cardToPlay, newPlayedCards[row][col]))
				newPlayedCards[row][col] = newPlayedCards[row][col].setPlayer(cardToPlay.card.holdingPlayer);
		}
	}
}
