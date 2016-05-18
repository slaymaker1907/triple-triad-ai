package com.dyllongagnier.triad.net;

public class VerifyOptionValidity extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private boolean isComplete;
	private boolean optionsValid;

	public VerifyOptionValidity()
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
	
	private void setOptionsValid(boolean optionsValid)
	{
		if (this.isComplete())
		{
			throw new UnsupportedOperationException();
		}
		this.optionsValid = optionsValid;
		this.setComplete();
	}
	
	public boolean optionsValid()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.optionsValid;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		try
		{
			server.getController().verifyOptionValidity();
			this.setOptionsValid(true);
		} catch (Exception e)
		{
			this.setOptionsValid(false);
		}
		server.sendMessage(this);
	}
}
