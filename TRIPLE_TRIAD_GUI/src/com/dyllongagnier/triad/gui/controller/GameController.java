package com.dyllongagnier.triad.gui.controller;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

public interface GameController
{
	public void setAgent(Player player, boolean isAI);
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename);
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
	public AscensionRule getAscensionRule();
	public void verifyOptionValidity();
	public void setTimeout(double timeout);
	public void resetAI();
	public TriadGame startNewGame();
	public void makeMove(PossibleMove move);
}