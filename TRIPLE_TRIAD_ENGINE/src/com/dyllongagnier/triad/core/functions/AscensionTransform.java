package com.dyllongagnier.triad.core.functions;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.dyllongagnier.triad.card.Card;

@FunctionalInterface
public interface AscensionTransform extends BiFunction<Card, Function<Card.Type, Integer>, Card>
{
	/**
	 * This function simply returns the input card since there is no ascension.
	 * @param card The card just played.
	 * @param typeCount The function to use for finding the number of type already played.
	 * @return The input card.
	 */
	public static Card noAscension(Card card, Function<Card.Type, Integer> typeCount)
	{
		return card;
	}
	
	/**
	 * This function returns the input card with its stats increased by 1 for each card of the
	 * same type already played.
	 * @param card The card just played.
	 * @param typeCount The function to use for finding the number of type already played.
	 * @return The input card with its new stats.
	 */
	public static Card ascension(Card card, Function<Card.Type, Integer> typeCount)
	{
		return card.increaseAllStats(typeCount.apply(card.cardType));
	}
	
	/**
	 * This function returns the input card with its stats decreased by 1 for each card of the
	 * same type already played.
	 * @param card The card just played.
	 * @param typeCount The function to use for finding the number of type already played.
	 * @return The input card with its new stats.
	 */
	public static Card descension(Card card, Function<Card.Type, Integer> typeCount)
	{
		return card.increaseAllStats(-1 * typeCount.apply(card.cardType));
	}
}
