package com.dyllongagnier.triad.gui.controller;

import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.TriadGame;

public class GUIAgent implements GameAgent
{
	public GUIAgent()
	{
	}
	
	@Override
	public GameAgent clone()
	{
		// Divide by two since there are two agents every game.
		return Players.getDefaultAI();
	}

	@Override
	public void takeTurn(TriadGame game)
	{
		throw new UnsupportedOperationException();
	}
}
