package com.dyllongagnier.triad.gui.view;

import java.awt.GridBagLayout;

import javax.swing.JFrame;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		JFrame window = new MainWindow();
		window.setVisible(true);
	}
	
	public MainWindow()
	{
		this.init();
	}
	
	private void init()
	{
		this.setSize(1000, 400);
		this.setLayout(new GridBagLayout());
		this.add(new CardWindow(CardList.getCard("Dodo").setHoldingPlayer(Player.NONE)));
	}
}
