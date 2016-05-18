package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.core.TriadGame;

public class StartNewGame extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private TriadGame newGame;

	public StartNewGame()
	{
		newGame = null;
	}
	
	public boolean isComplete()
	{
		return this.newGame != null;
	}
	
	public TriadGame getNewGame()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.newGame;
	}
	
	private void setNewGame(TriadGame newGame)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.newGame = newGame;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		this.setNewGame(server.getController().startNewGame());
		server.sendMessage(this);
	}
}
