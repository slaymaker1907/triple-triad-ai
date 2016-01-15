package com.dyllongagnier.triad.net;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.dyllongagnier.triad.gui.controller.GameController;

public class TriadServer
{
	private ServerPoint server;
	private int connId;
	private GameController controller;
	
	public TriadServer(int port) throws IOException
	{
		this.setPort(port);
		this.controller = GameController.getController(true);
	}
	
	public GameController getController()
	{
		return this.controller;
	}
	
	private void setPort(int port) throws IOException
	{
		this.server = new ServerPoint(port);
	}
	
	public void waitForClient() throws InterruptedException, ExecutionException
	{
		this.connId = this.server.acceptNewConn().get();
	}
	
	public Future<Boolean> sendMessage(Serializable toSend)
	{
		return this.server.sendObject(connId, toSend);
	}
}
