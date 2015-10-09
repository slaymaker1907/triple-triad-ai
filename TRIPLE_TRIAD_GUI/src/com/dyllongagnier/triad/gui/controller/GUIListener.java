package com.dyllongagnier.triad.gui.controller;

import javax.swing.SwingUtilities;

import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.TriadGame;

public class GUIListener implements GameListener
{
	protected static Runnable updateGUI(TriadGame newState, boolean isComplete)
	{
		Players.resetTimeout();
		throw new UnsupportedOperationException();
	}
	
	@Override
	public synchronized void gameChanged(TriadGame changedGame)
	{
		SwingUtilities.invokeLater(updateGUI(changedGame, false));
	}

	@Override
	public synchronized void gameComplete(TriadGame finalState)
	{
		SwingUtilities.invokeLater(updateGUI(finalState, true));
	}
}
