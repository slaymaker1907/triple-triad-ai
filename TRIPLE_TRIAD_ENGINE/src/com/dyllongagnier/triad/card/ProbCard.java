package com.dyllongagnier.triad.card;

/**
 * This class represents a possibility of a UndeployedCard being this card.
 */
public class ProbCard
{
	public final Card card;
	public final double probability;

	/**
	 * Initializes a ProbCard with the input parameters.
	 * 
	 * @param card
	 *            The card in question.
	 * @param probability
	 *            The probability of a deployed card being this card.
	 */
	public ProbCard(Card card, double probability)
	{
		assert card != null;
		assert probability >= 0 && probability <= 1;
		this.card = card;
		this.probability = probability;
	}

	@Override
	public boolean equals(Object o)
	{
		try
		{
			// It's probably ok to check probabilities according to == since
			// they should be exactly equal.
			ProbCard other = (ProbCard) o;
			return other.card.equals(this.card)
					&& this.probability == other.probability;
		} catch (ClassCastException e)
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return this.card.hashCode();
	}
}
