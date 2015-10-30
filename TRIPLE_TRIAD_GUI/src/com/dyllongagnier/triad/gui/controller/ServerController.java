package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.EnumMap;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.OrderedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

public class ServerController implements GameController
{
	private final EnumMap<Player, GUIAgent> agents;
	private BoardState.Builder gameBuilder = new BoardState.Builder();
	private TriadGame currentGame;
	
	ServerController()
	{
		this.agents = new EnumMap<>(Player.class);
		this.agents.put(Player.SELF, new GUIAgent(Player.SELF));
		this.agents.put(Player.OPPONENT, new GUIAgent(Player.OPPONENT));
		this.currentGame = null;
		this.setDefaultOptions();
	}
	
	@Override
	public void setAgent(Player player, boolean isAI)
	{
		this.agents.get(player).setIsAI(isAI);
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename) throws FileNotFoundException
	{
		switch(player)
		{
			case SELF:
			case OPPONENT:
				UndeployedCard[] result = HandFactory.getDeck(player, filename, null);
				this.gameBuilder.setHand(player, result);
				return Arrays.asList(result);
			default:
				throw new IllegalArgumentException("Player.NONE can not have a deck.");
		}
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player,
			String[] cardNames)
	{
		switch(player)
		{
			case SELF:
			case OPPONENT:
				UndeployedCard[] result = OrderedCard.convertToOrderedCard(CardList.generateHand(player, cardNames));
				this.gameBuilder.setHand(player, result);
				return Arrays.asList(result);
			default:
				throw new IllegalArgumentException("Player.NONE can not have a deck.");
		}
	}

	@Override
	public void setIsCombo(boolean isCombo)
	{
		this.gameBuilder.isCombo = isCombo;
	}

	@Override
	public void setIsFallenAce(boolean isFallenAce)
	{
		this.gameBuilder.isFallenAce = isFallenAce;
	}

	@Override
	public void setIsPlus(boolean isPlus)
	{
		this.gameBuilder.isPlus = isPlus;
	}

	@Override
	public void setIsReverse(boolean isReverse)
	{
		this.gameBuilder.isReverse = isReverse;
	}

	@Override
	public void setIsSame(boolean isSame)
	{
		this.gameBuilder.isSame = isSame;
	}

	@Override
	public void setAscensionRule(AscensionRule rule)
	{
		this.gameBuilder.setAscensionTransform(rule);
	}

	@Override
	public void setIsOrder(boolean isOrder)
	{
		this.gameBuilder.setIsOrder(isOrder);
	}

	@Override
	public void setIsSuddenDeath(boolean isSuddenDeath)
	{
		this.gameBuilder.isSuddenDeath = isSuddenDeath;
	}

	@Override
	public boolean getIsOrder()
	{
		return this.gameBuilder.getIsOrder();
	}

	@Override
	public void setDefaultOptions()
	{
		this.gameBuilder = new BoardState.Builder();
		this.setMaxThreads(Runtime.getRuntime().availableProcessors());
		this.setTimeout(8.0);
		this.setAgent(Player.SELF, true);
		this.setAgent(Player.OPPONENT, false);
	}

	@Override
	public void setMaxThreads(int maxThreads)
	{
		GUIAgent.setMaxThreads(maxThreads);
	}

	@Override
	public int getMaxThreads()
	{
		return GUIAgent.getMaxThreads();
	}

	@Override
	public boolean getIsCombo()
	{
		return this.gameBuilder.isCombo;
	}

	@Override
	public boolean getIsFallenAce()
	{
		return this.gameBuilder.isFallenAce;
	}

	@Override
	public boolean getIsPlus()
	{
		return this.gameBuilder.isPlus;
	}

	@Override
	public boolean getIsReverse()
	{
		return this.gameBuilder.isReverse;
	}

	@Override
	public boolean getIsSame()
	{
		return this.gameBuilder.isSame;
	}

	@Override
	public boolean getIsSuddenDeath()
	{
		return this.gameBuilder.isSuddenDeath;
	}

	@Override
	public double getTimeout()
	{
		return GUIAgent.getMaxTime() / 1000.0;
	}

	@Override
	public AscensionRule getAscensionRule()
	{
		return this.gameBuilder.getAscensionRule();
	}

	@Override
	public void verifyOptionValidity()
	{
		this.gameBuilder.build(Player.SELF);
	}

	@Override
	public void setTimeout(double timeout)
	{
		GUIAgent.setMaxThinkTime((long)(timeout * 1000.0));
	}

	@Override
	public TriadGame startNewGame()
	{
		TriadGame result = TriadGame.gameFactory(new PlayerGenerator(), gameBuilder,
				this.agents.get(Player.SELF), this.agents.get(Player.OPPONENT), new GUIListener());
		this.currentGame = result;
		result.startGame();
		return result;
	}

	@Override
	public void makeMove(PossibleMove move)
	{
		this.agents.get(this.currentGame.getCurrentPlayer()).makeMove(move);
	}
}
