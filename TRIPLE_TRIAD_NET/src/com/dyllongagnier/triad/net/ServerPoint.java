package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerPoint extends EndPoint
{
	private final ServerSocket socket;
	
	public ServerPoint(int port) throws IOException
	{
		this.socket = new ServerSocket(port);
	}
}
