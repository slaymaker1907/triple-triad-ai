package com.dyllongagnier.triad.gui.view;

import javax.swing.JPanel;

import com.dyllongagnier.triad.card.Player;

public class CurrentTurnIndicator extends JPanel
{
	private static final long serialVersionUID = 1L;

	public CurrentTurnIndicator(Player player)
	{
		super();
		this.setBackground(CardWindow.getPlayerColor(player));
	}
}
