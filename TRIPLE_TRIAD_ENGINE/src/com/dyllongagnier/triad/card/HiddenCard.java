package com.dyllongagnier.triad.card;

/**
 * This class represents 
 */
public abstract class HiddenCard implements UndeployedCard
{
	private static int idGen = 0;
	private static final Object idLock = new Object();
	
	private static int idFactory()
	{
		synchronized(idLock)
		{
			return idGen++;
		}
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
}
