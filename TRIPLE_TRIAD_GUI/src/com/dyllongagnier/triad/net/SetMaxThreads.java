package com.dyllongagnier.triad.net;

public class SetMaxThreads extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	private int threadCount;

	public SetMaxThreads(int threadCount)
	{
		this.setThreadCount(threadCount);
	}
	
	private void setThreadCount(int threadCount)
	{
		this.threadCount = threadCount;
	}
	
	public int getThreadCount()
	{
		return this.threadCount;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setMaxThreads(this.getThreadCount());
	}
}
