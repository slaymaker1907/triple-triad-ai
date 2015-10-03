package com.dyllongagnier.triad.ai;

import java.util.function.Supplier;

public class EvaluationWorker extends Thread
{
	public Supplier<Runnable> getMainWork, getQuickWork;
	public volatile boolean finishQuickly;
	
	public EvaluationWorker(Supplier<Runnable> getMainWork, Supplier<Runnable> getQuickWork)
	{
		this.getMainWork = getMainWork;
		this.getQuickWork = getQuickWork;
		this.finishQuickly = false;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			Runnable toRun;
			if (this.finishQuickly)
				toRun = this.getQuickWork.get();
			else
				toRun = this.getMainWork.get();
			
			// If it is null, stop execution of thread.
			if (toRun == null)
				return;
			else
				toRun.run();
		}
	}
	
	public void destroy()
	{
		this.finishQuickly = true;
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
