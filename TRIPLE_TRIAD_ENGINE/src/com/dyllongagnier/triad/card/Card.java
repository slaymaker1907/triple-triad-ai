package com.dyllongagnier.triad.card;

public class Card
{
	public static enum Type
	{
		BEASTMAN, PRIMAL, GARLEAN, SCION;
	}
	
	public final int north, east, south, west;
	public final Card.Type cardType;
	public final String cardName;
	public final int cardRarity;

	public Card(int north, int east, int south, int west, String cardName, Card.Type cardType, int cardRarity)
	{
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.cardType = cardType;
		this.cardName = cardName;
		this.cardRarity = cardRarity;
	}
}
