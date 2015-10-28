package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import com.dyllongagnier.triad.ai.FastSearchAI;
import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.OrderedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.RandomCard;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.card.ActionCard;
import com.dyllongagnier.triad.gui.view.MainWindow;

public class Players
{
	private static final GUIAgent selfAgent, opponentAgent;
	private static GameAgent defaultAI;
	private static BoardState.Builder gameBuilder = new BoardState.Builder();
	private static int maxThreads;
	private static long timeout = Long.MAX_VALUE;
	static TriadGame currentGame;
	
	static
	{
		maxThreads = Runtime.getRuntime().availableProcessors();
		defaultAI = new FastSearchAI(maxThreads);
		selfAgent = new GUIAgent(Player.SELF);
		opponentAgent = new GUIAgent(Player.OPPONENT);
		Players.setAgent(Player.SELF, true);
		Players.setAgent(Player.OPPONENT, false);
	}
	
	protected Players()
	{
	}
	
	public static void setAgent(Player player, boolean isAI)
	{
		switch(player)
		{
			case SELF:
				Players.selfAgent.setIsAI(isAI);
				break;
			case OPPONENT:
				Players.opponentAgent.setIsAI(isAI);
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
	
	public static Iterable<UndeployedCard> setPlayerDeck(Player player, String[] deck)
	{
		if (player == null)
			throw new NullPointerException();
		switch(player)
		{
			case SELF:
			case OPPONENT:
				UndeployedCard[] result = OrderedCard.convertToOrderedCard(CardList.generateHand(player, deck));
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
		Players.setTimeout(8);
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
	
	public static void resetAI()
	{
		((FastSearchAI)getDefaultAI()).destroy();
		Players.defaultAI = new FastSearchAI(Players.maxThreads);
	}
	
	/**
	 * This method starts a new game.
	 */
	public static TriadGame startNewGame()
	{
		TriadGame result = TriadGame.gameFactory(new PlayerGenerator(), gameBuilder, selfAgent, opponentAgent, new GUIListener());
		MainWindow.getMainWindow().displayBoardState(result);
		Players.resetTimeout();
		result.startGame();
		return result;
	}
	
	public static void makeMove(PossibleMove move)
	{
		GUIAgent agent = (GUIAgent)Players.getAgent(currentGame.getCurrentPlayer());
		agent.makeMove(move);
	}
}
