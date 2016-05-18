package com.dyllongagnier.triad.card;

import java.util.List;

public class OrderedCard implements UndeployedCard
{
	public final int order;
	private final UndeployedCard containedCard;
	
	public OrderedCard(UndeployedCard containedCard, int order)
	{
		this.order = order;
		this.containedCard = containedCard;
	}

	@Override
	public int compareTo(UndeployedCard o)
	{
		if (o instanceof OrderedCard)
		{
			return Integer.compare(this.hashCode(), o.hashCode());
		}
		else
		{
			return this.toString().compareTo(o.toString());
		}
	}

	@Override
	public Card deploy()
	{
		return this.containedCard.deploy();
	}

	@Override
	public List<ProbCard> getPossibleCards()
	{
		return this.containedCard.getPossibleCards();
	}

	@Override
	public UndeployedCard clone()
	{
		if (this.isVisible())
			return this;
		else
			return new OrderedCard(this.containedCard.clone(), this.order);
	}
	
	@Override
	public boolean isVisible()
	{
		return this.containedCard.isVisible();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof OrderedCard)
		{
			OrderedCard other = (OrderedCard)o;
			return other.order == this.order && other.containedCard.equals(this.containedCard);
		}
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.order;
	}
	
	public static UndeployedCard[] convertToOrderedCard(UndeployedCard[] cards)
	{
		UndeployedCard[] result = new UndeployedCard[cards.length];
		for(int i = 0; i < cards.length; i++)
			result[i] = new OrderedCard(cards[i].deploy(), i);
		return result;
	}
}
