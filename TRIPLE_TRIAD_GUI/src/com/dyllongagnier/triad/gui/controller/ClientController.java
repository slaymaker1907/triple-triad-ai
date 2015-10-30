package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

public class ClientController implements GameController
{

	@Override
	public void setAgent(Player player, boolean isAI)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename)
			throws FileNotFoundException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player,
			String[] cardNames)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIsCombo(boolean isCombo)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsFallenAce(boolean isFallenAce)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsPlus(boolean isPlus)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsReverse(boolean isReverse)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsSame(boolean isSame)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAscensionRule(AscensionRule rule)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsOrder(boolean isOrder)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIsSuddenDeath(boolean isSuddenDeath)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getIsOrder()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDefaultOptions()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxThreads(int maxThreads)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxThreads()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getIsCombo()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsFallenAce()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsPlus()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsReverse()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsSame()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsSuddenDeath()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getTimeout()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AscensionRule getAscensionRule()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verifyOptionValidity()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimeout(double timeout)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public TriadGame startNewGame()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeMove(PossibleMove move)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getIsAI(Player player)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
