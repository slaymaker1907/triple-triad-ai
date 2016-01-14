package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSocket
{
	private Socket socket;
	private ObjectOutputStream objectSender;
	private ObjectInputStream objectReceiv;
	private int id;
	private ExecutorService executor;
	private boolean ownsExecutor = false;
	
	private static final AtomicInteger idGen = new AtomicInteger(0);
	
	private static int generateId()
	{
		return idGen.incrementAndGet();
	}
	
	private void setId(int id)
	{
		this.id = id;
	}
	
	private boolean ownsExecutor()
	{
		return this.ownsExecutor;
	}
	
	private void setExecutor(ExecutorService exec, boolean owns)
	{
		if (exec== null)
			throw new NullPointerException();
		if (exec.isShutdown() || exec.isTerminated())
			throw new IllegalArgumentException("Executor must be running.");
		this.executor = exec;
		this.ownsExecutor = true;
	}
	
	private ExecutorService getExecutor()
	{
		return this.executor;
	}
	
	private void setSocket(Socket socket) throws IOException
	{
		if (socket == null)
			throw new NullPointerException();
		this.socket = socket;
		this.objectSender = new ObjectOutputStream(this.socket.getOutputStream());
		this.objectReceiv = new ObjectInputStream(this.socket.getInputStream());
	}
	
	// Assumed to be connected already.
	protected ObjectSocket(Socket socket, ExecutorService executor, boolean ownsExecutor) throws IOException
	{
		this.setId(ObjectSocket.generateId());
		this.setExecutor(executor, ownsExecutor);
		this.setSocket(socket);
	}
	
	public ObjectSocket(String address, int port) throws UnknownHostException, IOException
	{
		this(new Socket(address, port), Executors.newCachedThreadPool(), true);
	}
	
	public ObjectSocket(Socket socket, ExecutorService executor) throws IOException
	{
		this(socket, executor, false);
	}
	
	public Future<Serializable> getLastObject()
	{
		return this.getExecutor().submit(() -> (Serializable) this.objectReceiv.readObject());
	}
	
	public Future<Boolean> sendObject(Serializable serial)
	{
		return this.getExecutor().submit(() ->
		{
			try
			{
				this.objectSender.writeObject(serial);
				return true;
			}
			catch (Exception e) {return false;}
		});
	}
	
	public int getId()
	{
		return this.id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		try
		{
			ObjectSocket other = (ObjectSocket)o;
			return this.getId() == other.getId();
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return this.getId();
	}
	
	@Override
	protected void finalize()
	{	
		try{this.objectReceiv.close();}
		catch (Exception e){}
		try{this.objectSender.close();}
		catch (Exception e){}
		try{this.socket.close();}
		catch (Exception e){}
		if (this.ownsExecutor())
		{
			try{this.executor.shutdown();}
			catch (Exception e) {}
		}
	}
	
	public void close()
	{
		this.finalize();
	}
}
