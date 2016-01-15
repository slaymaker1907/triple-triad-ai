package com.dyllongagnier.triad.net;

public class GetMaxThreads extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private int maxThreads;
	private boolean isComplete;
	
	public GetMaxThreads()
	{
		this.isComplete = false;
	}
	
	private void setComplete()
	{
		this.isComplete = true;
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	
	private void setMaxThreads(int maxThreads)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.maxThreads = maxThreads;
		this.setComplete();
	}
	
	public int getMaxThreads()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.maxThreads;
	}

	@Override
	public void processObjectServer(TriadServer server)
	{
		this.setMaxThreads(server.getController().getMaxThreads());
		server.sendMessage(this);
	}
}
