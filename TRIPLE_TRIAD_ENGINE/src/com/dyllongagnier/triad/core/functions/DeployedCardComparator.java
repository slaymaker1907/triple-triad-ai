package com.dyllongagnier.triad.core.functions;

import java.util.Comparator;
import java.util.function.BiFunction;

import com.dyllongagnier.triad.card.DeployedCard;

/**
 * This class contains various comparators for deployed cards for different rule
 * sets.
 */
@FunctionalInterface
public interface DeployedCardComparator extends
		BiFunction<DeployedCard, DeployedCard, Boolean>
{
	/**
	 * This function compares playedCard with otherCard normally except that
	 * lower numbers beat bigger numbers.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean reverseCompare(DeployedCard playedCard,
			DeployedCard otherCard)
	{
		Comparator<Integer> comp = (i1, i2) -> -1 * Integer.compare(i1, i2);
		return DeployedCardComparator.compareCards(playedCard, otherCard, comp);
	}

	/**
	 * This function compares playedCard with otherCard normally except that 1
	 * beats 10 (A).
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean fallenAceCompare(DeployedCard playedCard,
			DeployedCard otherCard)
	{
		Comparator<Integer> comp = (i1, i2) ->
		{
			// If played card is 1 and other card is 10 (A) then return 1 since
			// 1 beats ten.
			if (i1 == 1 && i2 == 10)
				return 1;
			else if (i1 == 10 && i2 == 1)
				return -1;
			else
				return Integer.compare(i1, i2);
		};
		return DeployedCardComparator.compareCards(playedCard, otherCard, comp);
	}

	/**
	 * This function compares playedCard with otherCard using reverse rules
	 * except that 10 (A) beats 1.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean fallenAceReverseCompare(DeployedCard playedCard,
			DeployedCard otherCard)
	{
		Comparator<Integer> comp = (i1, i2) ->
		{
			// If played card is 10 and other card is 1 (A) then return 1 since
			// 10 beats 1.
			if (i1 == 10 && i2 == 1)
				return 1;
			else if (i1 == 1 && i2 == 10)
				return -1;
			else
				return -1 * Integer.compare(i1, i2);
		};
		return DeployedCardComparator.compareCards(playedCard, otherCard, comp);
	}

	/**
	 * This function compares playedCard with otherCard normally with higher
	 * numbers beating smaller ones.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean regularCompare(DeployedCard playedCard,
			DeployedCard otherCard)
	{
		return DeployedCardComparator.compareCards(playedCard, otherCard,
				Integer::compare);
	}

	/**
	 * This method captures otherCard if it's relevant stat is equal to
	 * playedCard's relevant stat.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The other card adjacent to just played.
	 * @return True if playedCard captures otherCard.
	 */
	public static boolean equalCompare(DeployedCard playedCard,
			DeployedCard otherCard)
	{
		assert playedCard.cardAdjacent(otherCard);
		return DeployedCardComparator.compareCards(playedCard, otherCard, (i1,
				i2) ->
		{
			// Hacky way to get the compareCards function to work since it
			// captures when i1 > i2.
				if (i1 == i2)
					return 1;
				else
					return -1;
			});
	}

	/**
	 * This function compares playedCard with otherCard using the supplied
	 * comparator to compare relevant integer attributes of the cards.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            The card already on the field.
	 * @return Whether the played card captures the other card.
	 */
	public static boolean compareCards(DeployedCard playedCard,
			DeployedCard otherCard, Comparator<Integer> comp)
	{
		DeployedCard.Direction dir = playedCard.getDirectionOfOther(otherCard);
		switch (dir)
		{
			case NORTH:
				return comp
						.compare(playedCard.card.north, otherCard.card.south) > 0;
			case EAST:
				return comp.compare(playedCard.card.east, otherCard.card.west) > 0;
			case SOUTH:
				return comp
						.compare(playedCard.card.south, otherCard.card.north) > 0;
			case WEST:
				return comp.compare(playedCard.card.west, otherCard.card.east) > 0;
			default:
				throw new RuntimeException("Unsupported enum value.");
		}
	}

	/**
	 * This method compares playedCard to otherCard and returns true if
	 * playedCard captures otherCard.
	 * 
	 * @param playedCard
	 *            The card just played.
	 * @param otherCard
	 *            A neighboring card that is already in play.
	 * @return True if playedCard captures otherCard.
	 */
	@Override
	public Boolean apply(DeployedCard playedCard, DeployedCard otherCard);
}
