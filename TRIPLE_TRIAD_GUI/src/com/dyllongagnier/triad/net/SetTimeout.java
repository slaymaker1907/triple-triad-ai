package com.dyllongagnier.triad.net;

public class SetTimeout extends NetworkObject
{
	private static final long serialVersionUID = 1L;

	private double timeout;
	
	public SetTimeout(double timeout)
	{
		this.setTimeout(timeout);
	}
	
	private void setTimeout(double timeout)
	{
		this.timeout = timeout;
	}
	
	public double getTimeout()
	{
		return this.timeout;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setTimeout(this.getTimeout());
	}
}
