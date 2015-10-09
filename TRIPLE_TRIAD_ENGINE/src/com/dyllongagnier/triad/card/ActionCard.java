package com.dyllongagnier.triad.card;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class ActionCard extends HiddenCard
{
	private RandomCard clone;
	private Collection<Card> possibleCards;
	private final Supplier<Card> action;

	/**
	 * Initializes a new RandomCard.
	 * 
	 * @param cards
	 *            Possible cards to draw from.
	 */
	public ActionCard(Collection<Card> cards, Supplier<Card> action)
	{
		if (cards == null)
			throw new NullPointerException();
		else if (cards.size() <= 0)
			throw new IllegalArgumentException("Must be one or more cards.");
		
		this.clone = new RandomCard(cards);
		this.possibleCards = new HashSet<>(cards);
		this.action = action;
	}

	@Override
	public List<ProbCard> getPossibleCards()
	{
		return clone.getPossibleCards();
	}
	
	private void removePossibleCard(Card toRemove)
	{
		this.possibleCards.remove(toRemove);
		if (this.possibleCards.size() > 0)
			this.clone = new RandomCard(this.possibleCards);
	}

	@Override
	public UndeployedCard clone()
	{
		return clone;
	}

	@Override
	public Card deploy()
	{
		Card result = this.action.get();
		this.removePossibleCard(result);
		return result;
	}
}
