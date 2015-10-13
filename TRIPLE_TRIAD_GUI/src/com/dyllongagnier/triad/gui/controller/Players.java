package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import com.dyllongagnier.triad.ai.FastSearchAI;
import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.RandomCard;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.card.ActionCard;

public class Players
{
	private static GameAgent selfAgent, opponentAgent;
	private static GameAgent defaultAI;
	private static BoardState.Builder gameBuilder = new BoardState.Builder();
	private static int maxThreads;
	private static long timeout = Long.MAX_VALUE;
	static TriadGame currentGame;
	
	static
	{
		maxThreads = Runtime.getRuntime().availableProcessors();
		defaultAI = new FastSearchAI(maxThreads);
		selfAgent = opponentAgent = defaultAI;
		Players.setAgent(Player.SELF, true);
		Players.setAgent(Player.OPPONENT, false);
	}
	
	protected Players()
	{
	}
	
	public static void setAgent(Player player, boolean isAI)
	{
		GameAgent agent;
		if (isAI)
			agent = Players.getDefaultAI();
		else
			agent = new GUIAgent(player);
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
	
	public static Iterable<UndeployedCard> setPlayerDeck(Player player, String filename) throws FileNotFoundException
	{
		if (player == null)
			throw new NullPointerException();
		switch(player)
		{
			case SELF:
			case OPPONENT:
				UndeployedCard[] result = HandFactory.getDeck(player, filename, Players.getGUIFunction(player));
				Players.gameBuilder.setHand(player, result);
				return Arrays.asList(result);
			default:
				throw new IllegalArgumentException("Player.NONE can not have a deck.");
		}
	}
	
	private static GameAgent getAgent(Player player)
	{
		GameAgent agent;
		switch(player)
		{
			case SELF:
				agent = Players.selfAgent;
				break;
			case OPPONENT:
				agent = Players.opponentAgent;
				break;
			default:
				throw new IllegalArgumentException("Player can not be NONE.");
		}
		
		return agent;
	}
	
	private static Function<Collection<Card>, UndeployedCard> getGUIFunction(Player player)
	{
		GameAgent agent = Players.getAgent(player);
		if (agent instanceof GUIAgent)
		{
			Supplier<Card> guiAction = ((GUIAgent)agent)::popLastMove;
			return (cards) -> new ActionCard(cards, guiAction);
		}
		else
		{
			return RandomCard::new;
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
		Players.setTimeout(Integer.MAX_VALUE);
	}
	
	static GameAgent getDefaultAI()
	{
		return defaultAI;
	}
	
	public static void setMaxThreads(int maxThreads)
	{
		if (maxThreads < 1)
			throw new IllegalArgumentException("Maximum number of threads must be greater than or equal to one.");
		Players.maxThreads = maxThreads;
		defaultAI = new FastSearchAI(maxThreads);
		if (Players.selfAgent instanceof FastSearchAI)
		{
			Players.selfAgent = defaultAI;
		}
		if (Players.opponentAgent instanceof FastSearchAI)
		{
			Players.opponentAgent = defaultAI;
		}
	}
	
	public static int getMaxThreads()
	{
		return Players.maxThreads;
	}
	
	public static void verifyOptionValidity()
	{
		Players.gameBuilder.build(Player.SELF);
	}
	
	public static void setIsSuddenDeath(boolean isSuddenDeath)
	{
		Players.gameBuilder.isSuddenDeath = isSuddenDeath;
	}
	
	/**
	 * This method sets the maximum think time for the AI.
	 * @param timeout
	 * 					Max think time in seconds.
	 */
	public static void setTimeout(double timeout)
	{
		Players.timeout = (long) (1000 * timeout);
	}
	
	/**
	 * This needs to be called every time the AI makes a move to reset the move timer.
	 */
	static void resetTimeout()
	{
		((FastSearchAI)getDefaultAI()).setMoveTimeout(timeout);
	}
	
	/**
	 * This method starts a new game.
	 */
	public static void startNewGame()
	{
		TriadGame.gameFactory(new PlayerGenerator(), gameBuilder, selfAgent, opponentAgent, new GUIListener()).startGame();
		Players.resetTimeout();
	}
	
	public static void makeMove(PossibleMove move)
	{
		GUIAgent agent = (GUIAgent)Players.getAgent(currentGame.getCurrentPlayer());
		agent.makeMove(move);
	}
}
