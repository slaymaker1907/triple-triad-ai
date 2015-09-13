package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;

@FunctionalInterface
public interface CardReplacementFunc
{
	/**
	 * This function replaces the input card with a new one.
	 * @param card The original card.
	 * @param row The original card's row.
	 * @param col The original card's column.
	 * @return A new replacement card.
	 */
	public Card replaceCard(Card card, int row, int col);
}
