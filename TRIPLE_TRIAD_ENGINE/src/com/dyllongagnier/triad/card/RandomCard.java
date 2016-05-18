package com.dyllongagnier.triad.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents a random card that is not known until it is played in
 * which all cards are equally likely.
 */
public class RandomCard extends HiddenCard
{
	private final List<ProbCard> possibleCards;
	private RandomCard copy = null;

	private static final Random gen = new Random();

	/**
	 * Initializes a new RandomCard.
	 * 
	 * @param cards
	 *            Possible cards to draw from.
	 */
	public RandomCard(Collection<Card> cards)
	{
		assert cards != null;
		assert cards.size() > 0;

		double probability = 1.0 / cards.size();
		ArrayList<ProbCard> possibleCards = new ArrayList<ProbCard>();
		for (Card card : cards)
		{
			assert card != null;
			possibleCards.add(new ProbCard(card, probability));
		}
		this.possibleCards = possibleCards;
	}

	/**
	 * This constructor takes toCopy and makes a copy of it.
	 * 
	 * @param toCopy
	 *            The cards to use for this object.
	 */
	protected RandomCard(List<ProbCard> toCopy)
	{
		this.possibleCards = new ArrayList<>(toCopy);
	}

	@Override
	public Card deploy()
	{
		ProbCard result = this.possibleCards.get(gen.nextInt(this.possibleCards
				.size()));
		boolean removed = this.possibleCards.remove(result);
		assert removed;
		return result.card;
	}

	@Override
	public List<ProbCard> getPossibleCards()
	{
		return Collections.unmodifiableList(this.possibleCards);
	}

	@Override
	public UndeployedCard clone()
	{
		// This is hacky way to ensure that every copy of this RandomCard
		// generates the same clone.
		// This is inherently not thread safe, though that shouldn't be an issue
		// as long as clone is only
		// called by one thread for any given object.
		if (this.copy != null)
			return this.copy;
		RandomCard result = new RandomCard(this.possibleCards);
		this.copy = result;
		return result;
	}
}
