package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.UndeployedCard;

public class DeployedCard 
{
	public final Card card;
	public final int row, col;
	
	public DeployedCard(UndeployedCard card, int row, int col)
	{
		assert BoardState.isInBounds(row, col);
		assert card != null;
		this.card = card.deploy();
		this.row = row;
		this.col = col;
	}
}
