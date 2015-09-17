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
	 * @param card The card in question.
	 * @param probability The probability of a deployed card being this card.
	 */
	public ProbCard(Card card, double probability)
	{
		this.card = card;
		this.probability = probability;
	}	
}
