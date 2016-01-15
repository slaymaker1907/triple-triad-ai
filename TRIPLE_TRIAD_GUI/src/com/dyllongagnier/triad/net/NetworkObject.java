package com.dyllongagnier.triad.net;

import java.io.Serializable;

public abstract class NetworkObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public void processObjectServer(TriadServer sendBack)
	{
		throw new UnsupportedOperationException();
	}
	
	public void processObjectClient()
	{
		throw new UnsupportedOperationException();
	}
}
