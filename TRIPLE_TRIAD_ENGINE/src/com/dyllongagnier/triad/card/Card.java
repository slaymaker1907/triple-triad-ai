package com.dyllongagnier.triad.card;

/**
 * An immutable representation of a card.
 */
public class Card
{
	/**
	 * The type/tribe of a card. Most of the time, this field does not matter. However, this field is used in ascension.
	 */
	public static enum Type
	{
		BEASTMAN, PRIMAL, GARLEAN, SCION, NONE;
	}
	
	public final int north, east, south, west;
	public final Card.Type cardType;
	public final String name;
	public final int cardRarity;

	/**
	 * This creates a new card object with the given parameters.
	 * @param north The top number on a card.
	 * @param east The right number on a card.
	 * @param south The bottom number on a card.
	 * @param west The left number on a card.
	 * @param cardName The name of the card. Must be non-null and should be unique.
	 * @param cardType The type/tribe of the card. Must be non-null.
	 * @param cardRarity The rarity rarity of the card. Must be 1-5.
	 */
	public Card(int north, int east, int south, int west, String cardName, Card.Type cardType, int cardRarity)
	{
		if (cardName == null)
			throw new NullPointerException();
		if (cardType == null)
			throw new NullPointerException();
		if (cardRarity < 1 || cardRarity > 5)
			throw new IllegalArgumentException("Card Rarity:" +  cardRarity);

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
