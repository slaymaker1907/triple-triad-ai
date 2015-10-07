package com.dyllongagnier.triad.ai;

import java.util.function.Consumer;

public class BoardNode implements Comparable<BoardNode>
{
	private final Consumer<NodeComm> expensiveEval, cheapEval;
	private final NodeComm comm;
	private final int heuristic;
	
	public BoardNode(NodeComm comm, Consumer<NodeComm> expensiveEval, Consumer<NodeComm> cheapEval, int heuristic)
	{
		this.comm = comm;
		this.cheapEval = cheapEval;
		this.expensiveEval = expensiveEval;
		this.heuristic = heuristic;
	}
	
	public void immediateEvaluation()
	{
		this.cheapEval.accept(comm);
	}
	
	public void regularEvaluation()
	{
		this.expensiveEval.accept(comm);
	}

	@Override
	public int compareTo(BoardNode o)
	{
		return Integer.compare(this.heuristic, o.heuristic);
	}
}
