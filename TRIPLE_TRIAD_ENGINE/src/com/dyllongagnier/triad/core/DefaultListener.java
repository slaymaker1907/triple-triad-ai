package com.dyllongagnier.triad.core;

/**
 * This class is a default listener for TriadGame that does absolutely nothing
 * and is provided as a convenient way for when a listener is required.
 */
public class DefaultListener implements GameListener
{
	@Override
	public void gameChanged(TriadGame changedGame)
	{
	}

	@Override
	public void gameComplete(TriadGame finalState)
	{
	}
}
