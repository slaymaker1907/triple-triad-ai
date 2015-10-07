package com.dyllongagnier.triad.ai;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.dyllongagnier.triad.core.PossibleMove;

public class NodeFutures
{
	private final HashMap<PossibleMove, Double> moveValue;
	private final Consumer<PossibleMove> moveMaker;
	private final int moveTotal;
	
	public NodeFutures(Consumer<PossibleMove> moveMaker, int moveTotal)
	{
		this.moveMaker = moveMaker;
		this.moveValue = new HashMap<>();
		this.moveTotal = moveTotal;
	}
	
	public void addNode(PossibleMove toAdd, Supplier<Integer> expensiveEval, Supplier<Integer> fastEval)
	{
		NodeComm comm = new NodeComm(moveTotal, (value) -> this.addResult(toAdd, value));
		BoardNode node = new BoardNode(comm, expensiveEval, fastEval);
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
