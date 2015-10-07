package com.dyllongagnier.triad.ai;

import java.util.function.Supplier;

public class BoardNode implements Comparable<BoardNode>
{
	private final Supplier<Integer> expensiveEval, cheapEval;
	private final NodeComm comm;
	private final int heuristic;
	
	public BoardNode(NodeComm comm, Supplier<Integer> expensiveEval, Supplier<Integer> cheapEval)
	{
		this.comm = comm;
		this.cheapEval = cheapEval;
		this.expensiveEval = expensiveEval;
		this.heuristic = this.cheapEval.get();
	}
	
	public int immediateEvaluation()
	{
		int result = this.cheapEval.get();
		this.comm.addResult(result);
		return result;
	}
	
	public int regularEvaluation()
	{
		int result = this.expensiveEval.get();
		this.comm.addResult(result);
		return result;
	}

	@Override
	public int compareTo(BoardNode o)
	{
		return Integer.compare(this.heuristic, o.heuristic);
	}
}
