package com.dyllongagnier.triad.ai;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NodeComm
{
	private final AtomicInteger resultCounter, result;
	private final Consumer<Double> resultAcceptor;
	private final int totalResults;
	
	public NodeComm(int totalResults, Consumer<Double> resultAcceptor)
	{
		this.resultCounter = new AtomicInteger(totalResults);
		this.result = new AtomicInteger(0);
		this.resultAcceptor = resultAcceptor;
		this.totalResults = totalResults;
	}
	
	public double addResult(int result)
	{
		double toReturn = this.result.addAndGet(result);
		if (this.resultCounter.decrementAndGet() <= 0)
			this.resultAcceptor.accept(toReturn / this.totalResults);
		return toReturn;
	}
}
