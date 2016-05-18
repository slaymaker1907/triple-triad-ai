package com.dyllongagnier.triad.ai;

class HitCounter
{
	private long startTime;
	private int queueSize;
	
	public HitCounter()
	{
		this.startTime = System.currentTimeMillis();
		this.queueSize = Integer.MAX_VALUE;
	}
	
	public synchronized void startTimer(int queueSize)
	{
		this.queueSize = queueSize;
		this.startTime = System.currentTimeMillis();
	}
	
	public synchronized double stopTimer()
	{
		double result = this.getNodesPerMilli();
		this.startTime = System.currentTimeMillis();
		return result;
	}
	
	private double getNodesPerMilli()
	{
		return 1.0 * this.queueSize / (System.currentTimeMillis() - this.startTime);
	}
}