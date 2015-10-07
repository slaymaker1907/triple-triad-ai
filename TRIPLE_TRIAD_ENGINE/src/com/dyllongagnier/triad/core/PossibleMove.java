package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.UndeployedCard;

public class PossibleMove
{
	public final UndeployedCard toPlay;
	public final int row, col;
	
	public PossibleMove(UndeployedCard toPlay, int row, int col)
	{
		this.toPlay = toPlay;
		this.row = row;
		this.col = col;
	}
}