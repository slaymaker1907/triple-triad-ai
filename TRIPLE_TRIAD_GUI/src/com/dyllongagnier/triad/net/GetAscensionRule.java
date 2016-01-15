package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.core.AscensionRule;

public class GetAscensionRule extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private boolean isComplete;
	private AscensionRule rule;
	
	public GetAscensionRule()
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
	
	private void setRule(AscensionRule rule)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.rule = rule;
		this.setComplete();
	}
	
	public AscensionRule getRule()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.rule;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		this.setRule(server.getController().getAscensionRule());
		server.sendMessage(this);
	}
}
