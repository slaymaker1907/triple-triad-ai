package com.dyllongagnier.triad.net;

public class SetDefaultOptions extends NetworkObject
{
	private static final long serialVersionUID = 1L;

	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setDefaultOptions();
	}
}
