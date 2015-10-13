package com.dyllongagnier.triad.ai;

public class BooleanReference
{
	private volatile boolean value;
	
	public BooleanReference()
	{
		this.value = false;
	}
	
	public BooleanReference(boolean value)
	{
		this.value = value;
	}
	
	public boolean get()
	{
		return this.value;
	}
	
	public void set(boolean value)
	{
		this.value = value;
	}
}
