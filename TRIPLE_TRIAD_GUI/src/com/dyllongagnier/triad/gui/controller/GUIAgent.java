package com.dyllongagnier.triad.gui.controller;

import java.util.List;

import com.dyllongagnier.triad.ai.FastSearchAI;
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
	private static FastSearchAI agentAI;
	
	static
	{
		GUIAgent.setMaxThreads(1);
	}
	
	public void setIsAI(boolean isAI)
	{
		if (this.isAI && !isAI)
		{
			GUIAgent.resetAI();
		}
		boolean isDiff = isAI ^ this.isAI;
		this.isAI = isAI;
		if (isDiff && this.gameInProgress())
		{
			this.takeTurn(this.currentGame);
		}
	}
	
	public static void setMaxThreads(int maxThreads)
	{
		long maxThinkTime;
		if (GUIAgent.agentAI != null)
			maxThinkTime = Long.MAX_VALUE;
		else
			maxThinkTime = 8_000;
		GUIAgent.agentAI = new FastSearchAI(maxThreads);
		GUIAgent.agentAI.setMoveTimeout(maxThinkTime);
	}
	
	public static long getMaxTime()
	{
		return GUIAgent.agentAI.getMaxTime();
	}
	
	private static void resetAI()
	{
		GUIAgent.agentAI.destroy();
		int maxThreads = GUIAgent.getMaxThreads();
		long maxTime = GUIAgent.getMaxTime();
		GUIAgent.setMaxThreads(maxThreads);
		GUIAgent.setMaxThinkTime(maxTime);
	}
	
	public static int getMaxThreads()
	{
		return GUIAgent.agentAI.getMaxThreads();
	}
	
	public static void setMaxThinkTime(long timeout)
	{
		GUIAgent.agentAI.setMoveTimeout(timeout);
	}
	
	public boolean gameInProgress()
	{
		if (currentGame == null)
			return false;
		return !currentGame.getCurrentState().gameComplete();
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
		return GUIAgent.agentAI;
	}

	@Override
	public synchronized void takeTurn(TriadGame game)
	{
		this.setCurrentGame(game);
		if (!this.isAI)
		{
			if (this.expectedPlayer != game.getCurrentPlayer())
				throw new InvalidPlayerException();
			MainWindow.getMainWindow().allowDraggingFromHand(expectedPlayer, true);
			MainWindow.getMainWindow().setCanDropToField(true);
		}
		else
		{
			MainWindow.getMainWindow().allowDraggingFromHand(expectedPlayer, false);
			MainWindow.getMainWindow().setCanDropToField(false);
			GUIAgent.resetAI();
			GUIAgent.agentAI.takeTurn(game);
		}
	}
	
	private void setCurrentGame(TriadGame game)
	{
		this.currentGame = game;
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
