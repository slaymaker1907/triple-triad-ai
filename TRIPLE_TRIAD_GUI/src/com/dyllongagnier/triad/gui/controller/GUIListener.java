package com.dyllongagnier.triad.gui.controller;

import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.gui.view.GameView;

public class GUIListener implements GameListener
{	
	@Override
	public synchronized void gameChanged(TriadGame changedGame)
	{
		GameView view = GameView.getGameView(true);
		view.displayBoard(changedGame.getCurrentState(), changedGame.getLastMove());
	}

	@Override
	public synchronized void gameComplete(TriadGame finalState)
	{
		GameView view = GameView.getGameView(true);
		view.displayBoard(finalState.getCurrentState(), finalState.getLastMove());
	}
}
