package com.dyllongagnier.triad.gui.controller;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.gui.view.MainWindow;

public class GUIListener implements GameListener
{
	protected static Runnable updateGUI(TriadGame newState, boolean isComplete)
	{
		return () ->
		{
			MainWindow.getMainWindow().displayBoardState(newState);
			if (!isComplete)
			{
				MainWindow.getMainWindow().allowDraggingFromHand(newState.getCurrentPlayer(), true);
				MainWindow.getMainWindow().setCanDropToField(true);
			}
			else
			{
				MainWindow.getMainWindow().setCurrentTurn(Player.NONE);
				JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Winner: " + newState.getCurrentState().getWinner());
			}
		};
	}
	
	@Override
	public synchronized void gameChanged(TriadGame changedGame)
	{
		Players.resetTimeout();
		SwingUtilities.invokeLater(updateGUI(changedGame, false));
	}

	@Override
	public synchronized void gameComplete(TriadGame finalState)
	{
		SwingUtilities.invokeLater(updateGUI(finalState, true));
	}
}
