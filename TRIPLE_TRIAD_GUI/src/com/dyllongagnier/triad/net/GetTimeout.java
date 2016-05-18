package com.dyllongagnier.triad.net;

public class GetTimeout extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private double timeout;
	private boolean isComplete;
	
	public GetTimeout()
	{
		this.isComplete = false;
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	
	private void setComplete()
	{
		this.isComplete = true;
	}
	
	private void setTimeout(double timeout)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.timeout = timeout;
		this.setComplete();
	}
	
	public double getTimeout()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.timeout;
	}

	@Override
	public void processObjectServer(TriadServer server)
	{
		this.setTimeout(server.getController().getTimeout());
		server.sendMessage(this);
	}
}
