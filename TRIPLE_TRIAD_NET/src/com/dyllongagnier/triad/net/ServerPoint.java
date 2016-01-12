package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerPoint extends EndPoint
{
	private final ServerSocket socket;
	private Executor exec = Executors.newCachedThreadPool();
	private final Map<Integer, ObjectSocket> clientMap = new HashMap<>();
	
	public ServerPoint(int port) throws IOException
	{
		this.socket = new ServerSocket(port);
		this.exec.execute(() -> this.acceptNewConn());
	}
	
	private void acceptNewConn()
	{
		try
		{
			Socket newSock = this.socket.accept();
			this.exec.execute(() -> this.handleNewConn(newSock));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.exec.execute(() -> this.acceptNewConn());
		}
	}
	
	private void handleNewConn(Socket socket)
	{
		try
		{
			ObjectSocket client = new ObjectSocket(socket);
			this.clientMap.put(client.getId(), client);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
