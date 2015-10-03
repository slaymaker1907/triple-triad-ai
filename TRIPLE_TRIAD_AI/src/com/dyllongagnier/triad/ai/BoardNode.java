package com.dyllongagnier.triad.ai;

import java.util.function.Supplier;

public class BoardNode
{
	private final Supplier<Integer> expensiveEval, cheapEval;
	private final NodeComm comm;
	
	public BoardNode(NodeComm comm, Supplier<Integer> expensiveEval, Supplier<Integer> cheapEval)
	{
		this.comm = comm;
		this.cheapEval = cheapEval;
		this.expensiveEval = expensiveEval;
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
}
