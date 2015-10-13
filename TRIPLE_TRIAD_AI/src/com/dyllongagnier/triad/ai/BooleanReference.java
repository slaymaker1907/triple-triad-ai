package com.dyllongagnier.triad.ai;

public class BooleanReference
{
	private volatile boolean value;
	private volatile TimerThread currentThread;
	
	private class TimerThread extends Thread
	{
		private final long timeout;
		private volatile boolean timerValid;
		
		public TimerThread(long timeout)
		{
			this.timeout = timeout;
			this.timerValid = true;
		}
		
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(timeout);
			} catch (InterruptedException e)
			{
			}
			
			if (timerValid)
				set(true);
		}
		
		public void invalidate()
		{
			timerValid = false;
		}
	}
	
	public BooleanReference()
	{
		this.value = false;
	}
	
	public BooleanReference(boolean value)
	{
		this.value = value;
	}
	
	public boolean get()
	{
		return this.value;
	}
	
	public void set(boolean value)
	{
		this.value = value;
	}
	
	public void setMaxTime(long timeout)
	{
		if (this.currentThread != null)
			this.currentThread.invalidate();
		this.currentThread = new TimerThread(timeout);
		this.currentThread.start();
	}
}
