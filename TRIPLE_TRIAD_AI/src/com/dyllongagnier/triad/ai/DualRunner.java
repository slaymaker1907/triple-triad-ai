package com.dyllongagnier.triad.ai;

public class DualRunner implements Comparable<DualRunner>
{
	public final Runnable fastRun, slowRun;
	private final int heuristic;
	
	public DualRunner(Runnable fastRun, Runnable slowRun, int heuristic)
	{
		this.fastRun = fastRun;
		this.slowRun = slowRun;
		this.heuristic = heuristic;
	}

	@Override
	public int compareTo(DualRunner o)
	{
		return Integer.compare(this.heuristic, o.heuristic);
	}
}
