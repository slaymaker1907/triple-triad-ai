package com.dyllongagnier.triad.gui.controller;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.PlayerSupplier;

public class PlayerGenerator implements PlayerSupplier
{
	private volatile Player toReturn;
	
	/**
	 * This method should query the user for the first player when sudden death occurs or at the start of the game.
	 * @return
	 */
	private static Player askUserForFirstPlayer()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized Player get()
	{
		// This may cause thread deadlock, be wary of this method.
		try
		{
			SwingUtilities.invokeAndWait(() -> this.toReturn = PlayerGenerator.askUserForFirstPlayer());
			Player toReturn = this.toReturn;
			this.toReturn = null;
			return toReturn;
		} catch (InvocationTargetException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public PlayerSupplier clone()
	{
		return Player::getRandomPlayer;
	}
}
