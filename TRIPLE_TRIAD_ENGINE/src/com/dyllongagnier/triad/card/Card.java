package com.dyllongagnier.triad.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An immutable representation of a card.
 */
public class Card implements UndeployedCard, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type/tribe of a card. Most of the time, this field does not matter.
	 * However, this field is used in ascension.
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
	 * This creates a new card object with the given parameters. It will
	 * initialize the player to Player.None.
	 * 
	 * @param north
	 *            The top number on a card.
	 * @param east
	 *            The right number on a card.
	 * @param south
	 *            The bottom number on a card.
	 * @param west
	 *            The left number on a card.
	 * @param cardName
	 *            The name of the card. Must be non-null and should be unique.
	 * @param cardType
	 *            The type/tribe of the card. Must be non-null.
	 * @param cardRarity
	 *            The rarity rarity of the card. Must be 1-5.
	 */
	public Card(int north, int east, int south, int west, String cardName,
			Card.Type cardType, int cardRarity)
	{
		assert cardName != null;
		assert cardType != null;
		assert cardRarity >= 1 && cardRarity <= 5;
		assert north > 0 && north <= 11;
		assert east > 0 && east <= 11;
		assert south > 0 && south <= 11;
		assert west > 0 && west <= 11;
		assert cardType != null;

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
	 * 
	 * @param north
	 *            The top number on a card.
	 * @param east
	 *            The right number on a card.
	 * @param south
	 *            The bottom number on a card.
	 * @param west
	 *            The left number on a card.
	 * @param cardName
	 *            The name of the card. Must be non-null and should be unique.
	 * @param cardType
	 *            The type/tribe of the card. Must be non-null.
	 * @param cardRarity
	 *            The rarity rarity of the card. Must be 1-5.
	 * @param holdingPlayer
	 *            The value to set holdingPlayer to. Must be non-null.
	 */
	public Card(int north, int east, int south, int west, String cardName,
			Card.Type cardType, int cardRarity, Player holdingPlayer)
	{
		assert cardName != null;
		assert cardType != null;
		assert cardRarity >= 1 && cardRarity <= 5;
		assert north > 0 && north <= 11;
		assert east > 0 && east <= 11;
		assert south > 0 && south <= 11;
		assert west > 0 && west <= 11;
		assert cardType != null;

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
	public boolean equals(Object o)
	{
		try
		{
			Card other = (Card) o;
			return this.name.equals(other.name)
					&& this.cardRarity == other.cardRarity
					&& this.cardType == other.cardType
					&& this.east == other.east
					&& this.holdingPlayer == other.holdingPlayer
					&& this.north == other.north && this.south == other.south
					&& this.west == other.west;
		} catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(this.name);
		builder.append(";");
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
		builder.append(';');
		builder.append(this.holdingPlayer.toString());
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		// Most of the time, there should only be two cards with different
		// players. The stats will generally
		// be the same, though, for the same card so it is not productive to use
		// those values.
		return this.name.hashCode() ^ (31 * this.holdingPlayer.hashCode());
	}

	/**
	 * This method returns a new Card without modifying the current card that is
	 * exactly the same as the current card except for the player.
	 * 
	 * @param input
	 *            The input player field.
	 * @return A new card.
	 */
	public Card setHoldingPlayer(Player input)
	{
		return new Card(this.north, this.east, this.south, this.west,
				this.name, this.cardType, this.cardRarity, input);
	}

	/**
	 * This method increases all stats (north, east, south, and west) by the
	 * indicated amount truncating any values greater than 10 to 10 and
	 * increasing any value < 1 to 1.
	 * 
	 * @param amount
	 *            The amount to increase by.
	 * @return A new card with the new stats that is otherwise identical to this
	 *         card.
	 */
	public Card increaseAllStats(int amount)
	{
		return new Card(checkedIncrease(this.north, amount), checkedIncrease(
				this.east, amount), checkedIncrease(this.south, amount),
				checkedIncrease(this.west, amount), this.name, this.cardType,
				this.cardRarity, this.holdingPlayer);
	}

	/**
	 * This method returns original + increase but truncates any result > 10 to
	 * 10 and any result < 1 to 1.
	 * 
	 * @param original
	 *            The original amount.
	 * @param increase
	 *            The amount to plus the original by.
	 * @return original + increase truncated to be 10 or less.
	 */
	private static int checkedIncrease(int original, int increase)
	{
		int result = original + increase;
		if (result >= 10)
			return 10;
		else if (result <= 1)
			return 1;
		else
			return result;
	}

	@Override
	public Card deploy()
	{
		return this;
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public int compareTo(UndeployedCard o)
	{
		return this.toString().compareTo(o.toString());
	}

	@Override
	public List<ProbCard> getPossibleCards()
	{
		ArrayList<ProbCard> result = new ArrayList<>(1);
		result.add(new ProbCard(this, 1));
		return result;
	}

	@Override
	public UndeployedCard clone()
	{
		// Since this is immutable, just return this object.
		return this;
	}
}
