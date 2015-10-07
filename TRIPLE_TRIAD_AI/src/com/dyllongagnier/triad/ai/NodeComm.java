package com.dyllongagnier.triad.ai;

import java.util.function.Consumer;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

public class NodeComm implements GameListener
{
	private int result, hits;
	private final Consumer<Double> resultAcceptor;
	private final int totalResults;
	private final Player currentPlayer;
	
	public NodeComm(Player currentPlayer, int totalResults, Consumer<Double> resultAcceptor)
	{
		this.result = 0;
		this.hits = 0;
		this.resultAcceptor = resultAcceptor;
		this.totalResults = totalResults;
		this.currentPlayer = currentPlayer;
	}
	
	public void addResult(BoardState state)
	{
		double toReturn;
		synchronized(this)
		{
			this.result += FastSearchAI.getCardsUnderPlayer(state, this.currentPlayer);
			this.hits++;
			toReturn = this.result;
			if (this.hits == totalResults)
				this.resultAcceptor.accept(toReturn / this.totalResults);
		}		
	}
	
	public void addQuickResult(BoardState state, PossibleMove move)
	{
		this.addResult(state.playCard(this.currentPlayer, move.toPlay, move.row, move.col));
	}

	@Override
	public void gameChanged(TriadGame changedGame)
	{
	}

	@Override
	public void gameComplete(TriadGame finalState)
	{
		this.addResult(finalState.getCurrentState());
	}
}
