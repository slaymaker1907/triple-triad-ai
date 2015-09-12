package com.dyllongagnier.triad.card;

public class Card
{
	public static enum Type
	{
		BEASTMAN, PRIMAL, GARLEAN, SCION;
	}
	
	public final int north, east, south, west;
	public final Card.Type cardType;

	public Card(int north, int east, int south, int west, Card.Type cardType)
	{
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.cardType = cardType;
	}
}
