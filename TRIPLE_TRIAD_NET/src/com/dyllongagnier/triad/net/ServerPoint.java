package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerPoint extends EndPoint
{
	private ServerSocket socket;
	private ExecutorService exec = Executors.newCachedThreadPool();
	private final Map<Integer, ObjectSocket> clientMap = new ConcurrentHashMap<>();
	
	public ServerPoint(int port) throws IOException
	{
		this.setPort(port);
	}
	
	private void setPort(int port) throws IOException
	{
		this.socket = new ServerSocket(port);
	}
	
	public Future<Integer> acceptNewConn()
	{
		return this.exec.submit(() -> {
			Socket newSock = this.socket.accept();
			return this.handleNewConn(newSock);
		});
	}
	
	private int handleNewConn(Socket socket) throws IOException
	{
		ObjectSocket client = new ObjectSocket(socket, this.exec);
		this.setClient(client);
		return client.getId();
	}
	
	private ObjectSocket getClient(int connId)
	{
		return this.clientMap.get(connId);
	}
	
	private void setClient(ObjectSocket socket)
	{
		this.clientMap.put(socket.getId(), socket);
	}
	
	public Future<Boolean> sendObject(int connId, Serializable message)
	{
		return this.getClient(connId).sendObject(message);
	}
	
	public Future<Serializable> getLastObject(int connId)
	{
		return this.getClient(connId).getLastObject();
	}
	
	@Override
	protected void finalize()
	{
		try
		{
			for(ObjectSocket sock : this.clientMap.values())
			{
				try{sock.close();}
				catch (Exception e){}
			}
		}
		catch (Exception e){}
		try{this.socket.close();}
		catch (Exception e){}
		try{this.exec.shutdown();}
		catch (Exception e){}
	}
	
	public void close()
	{
		this.finalize();
	}
}
