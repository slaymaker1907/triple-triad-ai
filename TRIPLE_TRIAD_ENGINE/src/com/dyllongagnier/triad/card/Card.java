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
	public final Player holdingPlayer;

	/**
	 * This creates a new card object with the given parameters. It will initialize the player to Player.None.
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
		this.holdingPlayer = Player.NONE;
	}
	
	/**
	 * This is a constructor which allows for every data type to be specified.
	 * @param north The top number on a card.
	 * @param east The right number on a card.
	 * @param south The bottom number on a card.
	 * @param west The left number on a card.
	 * @param cardName The name of the card. Must be non-null and should be unique.
	 * @param cardType The type/tribe of the card. Must be non-null.
	 * @param cardRarity The rarity rarity of the card. Must be 1-5.
	 * @param holdingPlayer The value to set holdingPlayer to. Must be non-null.
	 */
	public Card(int north, int east, int south, int west, String cardName, Card.Type cardType, int cardRarity, Player holdingPlayer)
	{
		if (cardName == null)
			throw new NullPointerException();
		if (cardType == null)
			throw new NullPointerException();
		if (holdingPlayer == null)
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
		this.holdingPlayer = holdingPlayer;
	}
	
	@Override
	public boolean equals(Object other)
	{
		try
		{
			Card otherCard = (Card)other;
			return this.toString().equals(otherCard.toString());
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("name;");
		builder.append(this.cardType);
		builder.append(';');
		builder.append(this.cardRarity);
		builder.append(';');
		builder.append(this.north);
		builder.append(';');
		builder.append(this.east);
		builder.append(';');
		builder.append(this.south);
		builder.append(';');
		builder.append(this.west);
		return builder.toString();
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	/**
	 * This method returns a new Card without modifying the current card that is exactly the same as the
	 * current card except for the player.
	 * @param input The input player field.
	 * @return A new card.
	 */
	public Card setHoldingPlayer(Player input)
	{
		return new Card(this.north, this.east, this.south, this.west, this.name, this.cardType, this.cardRarity, input);
	}
}
