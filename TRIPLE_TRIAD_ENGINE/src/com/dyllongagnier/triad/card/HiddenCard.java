package com.dyllongagnier.triad.card;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents
 */
public abstract class HiddenCard implements UndeployedCard
{
	private static final AtomicInteger idGen = new AtomicInteger(0);

	private static int idFactory()
	{
		return idGen.getAndIncrement();
	}

	public final int id;

	public HiddenCard()
	{
		this.id = HiddenCard.idFactory();
	}

	@Override
	public int compareTo(UndeployedCard arg0)
	{
		return this.toString().compareTo(arg0.toString());
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.id);
	}
	
	@Override
	public abstract UndeployedCard clone();
}
