package com.dyllongagnier.triad.gui.controller;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.PlayerSupplier;
import com.dyllongagnier.triad.gui.view.MainWindow;

public class PlayerGenerator implements PlayerSupplier
{
	private volatile Player toReturn;
	
	/**
	 * This method should query the user for the first player when sudden death occurs or at the start of the game.
	 * @return
	 */
	private static Player askUserForFirstPlayer()
	{
		Object[] possiblePlayers = new Object[]{Player.SELF, Player.OPPONENT};
		JOptionPane.showInputDialog(MainWindow.getMainWindow(), "Please enter the first player.", "First Player", JOptionPane.PLAIN_MESSAGE, null, possiblePlayers, Player.SELF);
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized Player get()
	{
		// This may cause thread deadlock, be wary of this method.
		try
		{
			if (!EventQueue.isDispatchThread())
				SwingUtilities.invokeAndWait(() -> this.toReturn = PlayerGenerator.askUserForFirstPlayer());
			else
				this.toReturn = PlayerGenerator.askUserForFirstPlayer();
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
