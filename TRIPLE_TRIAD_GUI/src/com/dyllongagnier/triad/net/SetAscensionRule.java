package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.core.AscensionRule;

public class SetAscensionRule extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private AscensionRule rule;

	public SetAscensionRule(AscensionRule rule)
	{
		this.setRule(rule);
	}
	
	private void setRule(AscensionRule rule)
	{
		this.rule = rule;
	}
	
	public AscensionRule getRule()
	{
		return this.rule;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setAscensionRule(rule);
	}
}
