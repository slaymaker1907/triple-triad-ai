package com.dyllongagnier.triad.gui.controller;

import java.util.List;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.gui.view.MainWindow;

public class GUIAgent implements GameAgent
{
	public final Player expectedPlayer;
	private volatile TriadGame currentGame;
	private volatile PossibleMove lastMove;
	private volatile boolean isAI;
	
	public void setIsAI(boolean isAI)
	{
		if (this.isAI && !isAI)
		{
			Players.resetAI();
		}
		this.isAI = isAI;
	}
	
	public boolean isAI()
	{
		return this.isAI;
	}
	
	public GUIAgent(Player expectedPlayer)
	{
		this.currentGame = null;
		this.expectedPlayer = expectedPlayer;
	}
	
	Card popLastMove()
	{
		PossibleMove result = lastMove;
		lastMove = null;
		return (Card)result.toPlay;
	}
	
	@Override
	public GameAgent clone()
	{
		// Divide by two since there are two agents every game.
		// Make default AI singleton.
		return Players.getDefaultAI();
	}

	@Override
	public synchronized void takeTurn(TriadGame game)
	{
		if (!this.isAI)
		{
			Players.currentGame = game;
			if (this.expectedPlayer != game.getCurrentPlayer())
				throw new InvalidPlayerException();
			this.currentGame = game;
			MainWindow.getMainWindow().allowDraggingFromHand(expectedPlayer, true);
			MainWindow.getMainWindow().setCanDropToField(true);
		}
		else
			Players.getDefaultAI().takeTurn(game);
	}
	
	public synchronized boolean canTakeTurn()
	{
		return this.currentGame == null;
	}
	
	public synchronized List<PossibleMove> getPossibleMoves()
	{
		if (this.currentGame == null)
			throw new InvalidPlayerException();
		return this.currentGame.getValidMoves();
	}
	
	/**
	 * This method is used by the GUI to take the GUI's turn.
	 * @param move
	 */
	public synchronized void makeMove(PossibleMove move)
	{
		if (this.currentGame == null)
			throw new InvalidPlayerException();
		else if (!(move.toPlay.isVisible()))
			throw new IllegalArgumentException("Move to play must be of a concrete Card.");
			
		this.lastMove = move;
		this.currentGame.takeTurn(move.toPlay, move.row, move.col);
		this.currentGame = null;
	}
}
