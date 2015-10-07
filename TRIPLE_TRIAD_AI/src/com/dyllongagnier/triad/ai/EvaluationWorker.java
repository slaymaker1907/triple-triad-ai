package com.dyllongagnier.triad.ai;

import java.util.function.Supplier;

public class EvaluationWorker extends Thread
{
	public Supplier<Runnable> getMainWork, getQuickWork;
	public volatile boolean finishQuickly;
	private volatile boolean isDestroyed;
	
	public EvaluationWorker(Supplier<Runnable> getMainWork, Supplier<Runnable> getQuickWork)
	{
		this.getMainWork = getMainWork;
		this.getQuickWork = getQuickWork;
		this.finishQuickly = false;
		this.isDestroyed = false;
	}
	
	@Override
	public void run()
	{
		while(!isDestroyed)
		{
			Runnable toRun;
			if (this.finishQuickly)
				toRun = this.getQuickWork.get();
			else
				toRun = this.getMainWork.get();
			
			assert toRun != null;
			toRun.run();
		}
	}
	
	public void destroy()
	{
		this.finishQuickly = true;
		this.isDestroyed = true;
		try
		{
			this.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
