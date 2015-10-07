package com.dyllongagnier.triad.ai;

import java.util.function.Consumer;

public class NodeComm
{
	private int result, hits;
	private final Consumer<Double> resultAcceptor;
	private final int totalResults;
	
	public NodeComm(int totalResults, Consumer<Double> resultAcceptor)
	{
		this.result = 0;
		this.hits = 0;
		this.resultAcceptor = resultAcceptor;
		this.totalResults = totalResults;
	}
	
	public double addResult(int result)
	{
		double toReturn;
		synchronized(this)
		{
			this.result += result;
			this.hits++;
			toReturn = this.result;
			if (this.hits == totalResults)
				this.resultAcceptor.accept(toReturn / this.totalResults);
		}
		
		return toReturn;
	}
}
