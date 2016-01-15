package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.core.PossibleMove;

public class MakeMove extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private PossibleMove move;
	
	public MakeMove(PossibleMove move)
	{
		this.setMove(move);
	}
	
	private void setMove(PossibleMove move)
	{
		this.move = move;
	}
	
	public PossibleMove getMove()
	{
		return this.move;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().makeMove(move);
	}
}
