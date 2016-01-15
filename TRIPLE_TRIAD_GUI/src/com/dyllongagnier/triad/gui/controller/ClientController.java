package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.net.*;
import com.dyllongagnier.triad.net.SetBoolean.*;
import com.dyllongagnier.triad.net.GetBoolean.*;

public class ClientController implements GameController
{
	private ObjectSocket socket;
	
	private void setAddress(String address, int port) throws UnknownHostException, IOException
	{
		this.socket = new ObjectSocket(address, port);
	}
	
	public ClientController(String address, int port) throws UnknownHostException, IOException
	{
		this.setAddress(address, port);
	}

	@Override
	public void setAgent(Player player, boolean isAi)
	{
		this.socket.sendObject(new SetAgent(player, isAi));
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename)
			throws FileNotFoundException
	{
		UndeployedCard[] result = HandFactory.getDeck(player, filename, null);
		this.socket.sendObject(new PlayerDeck(player, result));
		return Arrays.asList(result);
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player,
			String[] cardNames)
	{
		UndeployedCard[] result = CardList.generateHand(player, cardNames);
		this.socket.sendObject(new PlayerDeck(player, cardNames));
		return Arrays.asList(result);
	}

	@Override
	public void setIsCombo(boolean isCombo)
	{
		this.socket.sendObject(new SetIsCombo(isCombo));
	}

	@Override
	public void setIsFallenAce(boolean isFallenAce)
	{
		this.socket.sendObject(new SetIsFallenAce(isFallenAce));
	}

	@Override
	public void setIsPlus(boolean isPlus)
	{
		this.socket.sendObject(new SetIsPlus(isPlus));
	}

	@Override
	public void setIsReverse(boolean isReverse)
	{
		this.socket.sendObject(new SetIsReverse(isReverse));
	}

	@Override
	public void setIsSame(boolean isSame)
	{
		this.socket.sendObject(new SetIsSame(isSame));
	}

	@Override
	public void setAscensionRule(AscensionRule rule)
	{
		this.socket.sendObject(new SetAscensionRule(rule));
	}

	@Override
	public void setIsOrder(boolean isOrder)
	{
		this.socket.sendObject(new SetIsOrder(isOrder));
	}

	@Override
	public void setIsSuddenDeath(boolean isSuddenDeath)
	{
		this.socket.sendObject(new SetIsSuddenDeath(isSuddenDeath));
	}

	@Override
	public boolean getIsOrder()
	{
		this.socket.sendObject(new GetIsCombo());
		try
		{
			GetIsCombo result = (GetIsCombo)this.socket.getLastObject().get();
			return result.getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDefaultOptions()
	{
		this.socket.sendObject(new SetDefaultOptions());
	}

	@Override
	public void setMaxThreads(int maxThreads)
	{
		this.socket.sendObject(new SetMaxThreads(maxThreads));
	}

	@Override
	public int getMaxThreads()
	{
		this.socket.sendObject(new GetMaxThreads());
		try
		{
			GetMaxThreads result = (GetMaxThreads)this.socket.getLastObject().get();
			return result.getMaxThreads();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsCombo()
	{
		this.socket.sendObject(new GetIsCombo());
		try
		{
			return ((GetIsCombo)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsFallenAce()
	{
		this.socket.sendObject(new GetIsFallenAce());
		try
		{
			return ((GetIsFallenAce)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsPlus()
	{
		this.socket.sendObject(new GetIsPlus());
		try
		{
			return ((GetIsPlus)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsReverse()
	{
		this.socket.sendObject(new GetIsReverse());
		try
		{
			return ((GetIsReverse)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsSame()
	{
		this.socket.sendObject(new GetIsSame());
		try
		{
			return ((GetIsSame)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsSuddenDeath()
	{
		this.socket.sendObject(new GetIsSuddenDeath());
		try
		{
			return ((GetIsSuddenDeath)this.socket.getLastObject().get()).getValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getTimeout()
	{
		this.socket.sendObject(new GetTimeout());
		try
		{
			return ((GetTimeout)this.socket.getLastObject().get()).getTimeout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public AscensionRule getAscensionRule()
	{
		this.socket.sendObject(new GetAscensionRule());
		try
		{
			return ((GetAscensionRule)this.socket.getLastObject().get()).getRule();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void verifyOptionValidity() throws InterruptedException, ExecutionException
	{
		this.socket.sendObject(new VerifyOptionValidity());
		if (!((VerifyOptionValidity)this.socket.getLastObject().get()).optionsValid())
			throw new IllegalArgumentException("Options aren't valid.");
	}

	@Override
	public void setTimeout(double timeout)
	{
		this.socket.sendObject(new SetTimeout(timeout));
	}

	@Override
	public TriadGame startNewGame()
	{
		this.socket.sendObject(new StartNewGame());
		try
		{
			return ((StartNewGame)this.socket.getLastObject().get()).getNewGame();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void makeMove(PossibleMove move)
	{
		this.socket.sendObject(new MakeMove(move));
	}

	@Override
	public boolean getIsAI(Player player)
	{
		this.socket.sendObject(new GetIsAi(player));
		try
		{
			return ((GetIsAi)this.socket.getLastObject().get()).isAi();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
