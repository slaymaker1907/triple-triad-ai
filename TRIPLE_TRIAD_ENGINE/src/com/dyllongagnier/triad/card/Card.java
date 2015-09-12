package com.dyllongagnier.triad.card;

public class Card
{
	public static enum Type
	{
		BEASTMAN, PRIMAL, GARLEAN, SCION, NONE;
	}
	
	public final int north, east, south, west;
	public final Card.Type cardType;
	public final String name;
	public final int cardRarity;

	public Card(int north, int east, int south, int west, String cardName, Card.Type cardType, int cardRarity)
	{
		if (cardName == null)
			throw new NullPointerException();
		if (cardType == null)
			throw new NullPointerException();
		
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.cardType = cardType;
		this.name = cardName;
		this.cardRarity = cardRarity;
	}
	
	@Override
	public boolean equals(Object other)
	{
		try
		{
			Card otherCard = (Card)other;
			return this.name.equals(otherCard.name);
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
