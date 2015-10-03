package com.dyllongagnier.triad.ai;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NodeComm
{
	private final AtomicInteger resultCounter, result;
	private final Consumer<Integer> resultAcceptor;
	
	public NodeComm(int totalResults, Consumer<Integer> resultAcceptor)
	{
		this.resultCounter = new AtomicInteger(totalResults);
		this.result = new AtomicInteger(0);
		this.resultAcceptor = resultAcceptor;
	}
	
	public int addResult(int result)
	{
		int toReturn = this.result.addAndGet(result);
		if (this.resultCounter.decrementAndGet() <= 0)
			this.resultAcceptor.accept(toReturn);
		return toReturn;
	}
}
