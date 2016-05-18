package com.dyllongagnier.triad.gui.controller;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.PlayerSupplier;
import com.dyllongagnier.triad.gui.view.GameView;

public class PlayerGenerator implements PlayerSupplier
{
	@Override
	public synchronized Player get()
	{	
		return GameView.getGameView(true).getFirstPlayer();
	}
	
	@Override
	public PlayerSupplier clone()
	{
		return Player::getRandomPlayer;
	}
}
