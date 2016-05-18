package com.dyllongagnier.triad.core.functions;

import java.util.function.BiFunction;

import com.dyllongagnier.triad.card.DeployedCard;

@FunctionalInterface
public interface AscensionTransform extends
		BiFunction<DeployedCard, Integer, DeployedCard>
{
	/**
	 * This function simply returns the input card since there is no ascension.
	 * 
	 * @param card
	 *            The card just played.
	 * @param amount
	 *            Unused.
	 * @return The input card.
	 */
	public static DeployedCard noAscension(DeployedCard card, int amount)
	{
		return card;
	}

	/**
	 * This function returns the input card with its stats increased by the
	 * amount input
	 * 
	 * @param card
	 *            The card just played.
	 * @param amount
	 *            The amount to increase the card's stats by.
	 * @return The input card with its new stats.
	 */
	public static DeployedCard ascension(DeployedCard card, int amount)
	{
		return new DeployedCard(card.card.increaseAllStats(amount), card.row,
				card.col);
	}

	/**
	 * This function returns the input card with its stats decreased by 1 for
	 * each card of the same type already played.
	 * 
	 * @param card
	 *            The card just played.
	 * @param The
	 *            amount to decrease the card's stats by.
	 * @return The input card with its new stats.
	 */
	public static DeployedCard descension(DeployedCard card, int amount)
	{
		return AscensionTransform.ascension(card, -1 * amount);
	}
}
