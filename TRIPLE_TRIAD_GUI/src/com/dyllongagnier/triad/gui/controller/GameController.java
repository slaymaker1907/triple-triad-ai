package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

public interface GameController
{
	public void setAgent(Player player, boolean isAI);
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename) throws FileNotFoundException;
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String[] cardNames);
	public void setIsCombo(boolean isCombo);
	public void setIsFallenAce(boolean isFallenAce);
	public void setIsPlus(boolean isPlus);
	public void setIsReverse(boolean isReverse);
	public void setIsSame(boolean isSame);
	public void setAscensionRule(AscensionRule rule);
	public void setIsOrder(boolean isOrder);
	public void setIsSuddenDeath(boolean isSuddenDeath);
	public boolean getIsOrder();
	public void setDefaultOptions();
	public void setMaxThreads(int maxThreads);
	public int getMaxThreads();
	public boolean getIsCombo();
	public boolean getIsFallenAce();
	public boolean getIsPlus();
	public boolean getIsReverse();
	public boolean getIsSame();
	public boolean getIsSuddenDeath();
	public double getTimeout();
	public boolean getIsAI(Player player);
	public AscensionRule getAscensionRule();
	public void verifyOptionValidity() throws Exception;
	public void setTimeout(double timeout);
	public TriadGame startNewGame();
	public void makeMove(PossibleMove move);
	
	static final ServerController serverController = new ServerController();
	
	public static GameController getController(boolean isServer)
	{
		// TODO Actually make this choose a different controller depending on input.
		return GameController.serverController;
	}
}