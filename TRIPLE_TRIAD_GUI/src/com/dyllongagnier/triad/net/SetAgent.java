package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.card.Player;

public class SetAgent extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private Player player;
	private boolean isAi;

	public SetAgent(Player player, boolean isAi)
	{
		this.setPlayer(player);
		this.setAi(isAi);
	}
	
	private void setPlayer(Player player)
	{
		this.player = player;
	}
	
	private void setAi(boolean isAi)
	{
		this.isAi = isAi;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public boolean isAi()
	{
		return this.isAi;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setAgent(this.getPlayer(), this.isAi());
	}
}
