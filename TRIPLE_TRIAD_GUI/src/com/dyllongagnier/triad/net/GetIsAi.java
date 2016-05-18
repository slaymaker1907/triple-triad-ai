package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.card.Player;

public class GetIsAi extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private Player player;
	private boolean isAi;
	private boolean isComplete;

	public GetIsAi(Player player)
	{
		this.isComplete = false;
		this.setPlayer(player);
	}
	
	private void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	
	private void setComplete()
	{
		this.isComplete = true;
	}
	
	private void setIsAi(boolean isAi)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.isAi = isAi;
		this.setComplete();
	}
	
	public boolean isAi()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.isAi;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		this.setIsAi(server.getController().getIsAI(this.getPlayer()));
		server.sendMessage(this);
	}
}
