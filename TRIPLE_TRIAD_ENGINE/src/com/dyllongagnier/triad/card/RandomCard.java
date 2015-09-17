package com.dyllongagnier.triad.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents a random card that is not known until it is played in which
 * all cards are equally likely.
 */
public class RandomCard extends HiddenCard
{
	private final List<ProbCard> possibleCards;
	private static final Random gen = new Random();
	
	/**
	 * Initializes a new RandomCard.
	 * @param cards Possible cards to draw from.
	 */
	public RandomCard(Collection<Card> cards)
	{
		ArrayList<ProbCard> possibleCards = new ArrayList<ProbCard>();
		for(Card card : cards)
			possibleCards.add(new ProbCard(card, 1.0 / cards.size()));
		this.possibleCards = Collections.unmodifiableList(possibleCards);
	}

	@Override
	public Card deploy() 
	{
		return this.possibleCards.get(gen.nextInt(this.possibleCards.size())).card;
	}

	@Override
	public List<ProbCard> getPossibleCards() 
	{
		return this.possibleCards;
	}

}
