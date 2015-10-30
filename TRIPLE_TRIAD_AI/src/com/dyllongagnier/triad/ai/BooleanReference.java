package com.dyllongagnier.triad.ai;

import java.util.function.Supplier;

public class BooleanReference
{
	private volatile boolean value;
	private final HitCounter counter = new HitCounter();
	private long maxTime, startTime;
	private final Supplier<Integer> getQueueSize;
	private double nodesPerMilli;
	
	private static final double SLOWEST_THINK_TIME = 100;
	
	public BooleanReference(Supplier<Integer> getQueueSize)
	{
		this.getQueueSize = getQueueSize;
		this.setMaxTime(Long.MAX_VALUE);
		this.nodesPerMilli = SLOWEST_THINK_TIME;
	}
	
	public boolean get()
	{
		this.updateCounter();
		return this.value;
	}
	
	private void updateCounter()
	{
		if (!this.value && this.calculationOver())
		{
			this.value = true;
			this.counter.startTimer(this.getQueueSize.get());
		}
	}
	
	private boolean calculationOver()
	{
		long elapsedTime = this.getElapsedTime();
		long expectedDelay = (long)(this.getQueueSize.get() / this.nodesPerMilli);
		long adjustedTime = elapsedTime + expectedDelay;
		return adjustedTime > this.maxTime;
	}
	
	private long getElapsedTime()
	{
		return System.currentTimeMillis() - this.startTime;
	}
	
	public void set(boolean value)
	{
		if (this.value && !value)
		{
			this.setEstimation();
		}
		this.value = value;
	}
	
	private void setEstimation()
	{
		double newTime = this.counter.stopTimer();
		if (newTime > BooleanReference.SLOWEST_THINK_TIME)
			this.nodesPerMilli = newTime;
	}
	
	public synchronized void setMaxTime(long timeout)
	{
		this.maxTime = timeout;
		this.startTime = System.currentTimeMillis();
	}
	
	public long getTimeout()
	{
		return this.maxTime;
	}
}
