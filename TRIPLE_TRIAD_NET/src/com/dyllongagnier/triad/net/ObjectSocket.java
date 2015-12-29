package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ObjectSocket
{
	private final Socket socket;
	private final ObjectOutputStream objectSender;
	private final ObjectInputStream objectReceiv;
	
	// Assumed to be connected already.
	public ObjectSocket(Socket socket) throws IOException
	{
		this.socket = socket;
		this.objectSender = new ObjectOutputStream(this.socket.getOutputStream());
		this.objectReceiv = new ObjectInputStream(this.socket.getInputStream());
	}
	
	public void send(Serializable toSend) throws IOException
	{
		this.objectSender.writeObject(toSend);
	}
	
	public Serializable getLastObject() throws ClassNotFoundException, IOException
	{
		return (Serializable) this.objectReceiv.readObject();
	}
}
