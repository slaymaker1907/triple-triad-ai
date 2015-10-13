package com.dyllongagnier.triad.gui.controller;

import javax.swing.SwingUtilities;

import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.gui.view.MainWindow;

public class GUIListener implements GameListener
{
	protected static Runnable updateGUI(TriadGame newState, boolean isComplete)
	{
		return () ->
		{
			Players.resetTimeout();
			MainWindow.getMainWindow().displayBoardState(newState);
		};
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
