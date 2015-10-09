package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;

import com.dyllongagnier.triad.ai.FastSearchAI;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;

public class Players
{
	private static GameAgent selfAgent, opponentAgent;
	private static BoardState.Builder gameBuilder;
	private static int maxThreads;
	
	protected Players()
	{
	}
	
	public static void setAgent(Player player, boolean isAI)
	{
		GameAgent agent;
		if (isAI)
			agent = Players.getDefaultAI();
		else
			agent = new GUIAgent();
		switch(player)
		{
			case SELF:
				Players.selfAgent = agent;
				break;
			case OPPONENT:
				Players.opponentAgent = agent;
				break;
			default:
				throw new IllegalArgumentException("Player.NONE can not have an agent.");
		}
	}
	
	public static void setPlayerDeck(Player player, String filename) throws FileNotFoundException
	{
		if (player == null)
			throw new NullPointerException();
		switch(player)
		{
			case SELF:
			case OPPONENT:
				Players.gameBuilder.setHand(player, HandFactory.getDeck(player, filename));
				break;
			default:
				throw new IllegalArgumentException("Player.NONE can not have a deck.");
		}
	}
	
	public static void setIsCombo(boolean isCombo)
	{
		Players.gameBuilder.isCombo = isCombo;
	}
	
	public static void setIsFallenAce(boolean isFallenAce)
	{
		Players.gameBuilder.isFallenAce = isFallenAce;
	}
	
	public static void setIsPlus(boolean isPlus)
	{
		Players.gameBuilder.isPlus = isPlus;
	}
	
	public static void setIsReverse(boolean isReverse)
	{
		Players.gameBuilder.isReverse = isReverse;
	}
	
	public static void setIsSame(boolean isSame)
	{
		Players.gameBuilder.isSame = isSame;
	}
	
	public static void setAscensionRule(AscensionRule rule)
	{
		if (rule == null)
			throw new NullPointerException();
		Players.gameBuilder.setAscensionTransform(rule);
	}
	
	public static void setIsOrder(boolean isOrder)
	{
		Players.gameBuilder.setIsOrder(isOrder);
	}
	
	public static void setDefaultOptions()
	{
		Players.gameBuilder = new BoardState.Builder();
		Players.setMaxThreads(Runtime.getRuntime().availableProcessors());
	}
	
	static GameAgent getDefaultAI()
	{
		return new FastSearchAI(Players.getMaxThreads() / 2);
	}
	
	public static void setMaxThreads(int maxThreads)
	{
		if (maxThreads < 1)
			throw new IllegalArgumentException("Maximum number of threads must be greater than or equal to one.");
		Players.maxThreads = maxThreads;
	}
	
	public static int getMaxThreads()
	{
		return Players.maxThreads;
	}
	
	public static void verifyOptionValidity()
	{
		Players.gameBuilder.build();
	}
}
