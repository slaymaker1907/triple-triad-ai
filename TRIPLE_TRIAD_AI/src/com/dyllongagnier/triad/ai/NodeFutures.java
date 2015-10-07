package com.dyllongagnier.triad.ai;

import java.util.HashMap;
import java.util.function.Consumer;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.PossibleMove;

public class NodeFutures
{
	private final HashMap<PossibleMove, Double> moveValue;
	private final Consumer<PossibleMove> moveMaker;
	private final int moveTotal;
	private final Player currentPlayer;
	
	public NodeFutures(Player currentPlayer, Consumer<PossibleMove> moveMaker, int moveTotal)
	{
		this.moveMaker = moveMaker;
		this.moveValue = new HashMap<>();
		this.moveTotal = moveTotal;
		this.currentPlayer = currentPlayer;
	}
	
	public void addNode(PossibleMove toAdd, Consumer<NodeComm> expensiveEval, Consumer<NodeComm> fastEval, int heuristic)
	{
		NodeComm comm = new NodeComm(currentPlayer, moveTotal, (value) -> this.addResult(toAdd, value));
		BoardNode node = new BoardNode(comm, expensiveEval, fastEval, heuristic);
		EvaluationQueue.addNodeToQueue(node);
	}
	
	private void addResult(PossibleMove move, Double value)
	{
		synchronized(this.moveValue)
		{
			this.moveValue.put(move, value);
			if (this.moveValue.size() >= moveTotal)
				this.moveMaker.accept(this.getBestMove());
		}
	}
	
	public int size()
	{
		return this.moveValue.size();
	}
	
	private PossibleMove getBestMove()
	{
		PossibleMove bestMove = null;
		double bestVal = -100_000.0;
		for(PossibleMove move : this.moveValue.keySet())
		{
			double temp = this.moveValue.get(move);
			if (temp > bestVal)
			{
				bestMove = move;
				bestVal = temp;
			}
		}
		
		return bestMove;
	}
}
