package com.dyllongagnier.triad.core.functions;

import java.util.Comparator;

import com.dyllongagnier.triad.core.DeployedCard;

/**
 * This class contains various comparators for deployed cards for different rule sets.
 */
public class DeployedCardComparators 
{
	/**
	 * This function compares playedCard with otherCard normally except that lower numbers beat
	 * bigger numbers.
	 * @param playedCard The card just played.
	 * @param otherCard The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean reverseCompare(DeployedCard playedCard, DeployedCard otherCard)
	{
		Comparator<Integer> comp = (i1, i2) -> -1 * Integer.compare(i1, i2);
		return DeployedCardComparators.compareCards(playedCard, otherCard, comp);
	}
	
	/**
	 * This function compares playedCard with otherCard normally with higher numbers beating smaller ones.
	 * @param playedCard The card just played.
	 * @param otherCard The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean regularCompare(DeployedCard playedCard, DeployedCard otherCard)
	{
		return DeployedCardComparators.compareCards(playedCard, otherCard, Integer::compare);
	}
	
	/**
	 * This function compares playedCard with otherCard using the supplied comparator to compare relevant
	 * integer attributes of the cards.
	 * @param playedCard The card just played.
	 * @param otherCard The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	private static boolean compareCards(DeployedCard playedCard, DeployedCard otherCard, Comparator<Integer> comp)
	{
		DeployedCard.Direction dir = playedCard.getDirectionOfOther(otherCard);
		switch(dir)
		{
		case NORTH:
			return comp.compare(playedCard.card.north, otherCard.card.south) > 0;
		case EAST:
			return comp.compare(playedCard.card.east, otherCard.card.west) > 0;
		case SOUTH:
			return comp.compare(playedCard.card.south, otherCard.card.north) > 0;
		case WEST:
			return comp.compare(playedCard.card.west, otherCard.card.east) > 0;
		default:
			throw new RuntimeException("Unsupported enum value.");
		}
	}
}
