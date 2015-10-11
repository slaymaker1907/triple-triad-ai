package com.dyllongagnier.triad.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;


public class CardCollection extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static int defaultSize = 100;
	private final CardWindow[] cards;

	public CardCollection()
	{
		this.cards = new CardWindow[9];
		this.init();
	}
	
	public static int getDefaultSize()
	{
		return CardWindow.getDefaultSize() * 3;
	}
	
	private int getNumRows()
	{
		if (cards.length % 3 == 0)
			return cards.length / 3;
		else
			return cards.length / 3 + 1;
	}
	
	private void init()
	{
		this.setLayout(new GridLayout(this.getNumRows(), 3));
		Dimension square = new Dimension(CardCollection.defaultSize, CardCollection.defaultSize);
		this.setMaximumSize(square);
		this.setSize(square);
		this.setMinimumSize(square);
		for(int i = 0; i < this.cards.length; i++)
			this.cards[i] = new CardWindow();
		this.addCards();
		this.setBackground(Color.GREEN);
		this.repaint();
	}
	
	private void addCards()
	{
		this.removeAll();
		for(CardWindow window : this.cards)
			this.add(window);
	}
	
	public void setCard(CardWindow card, int row, int col)
	{
		this.setCard(card, row * 3 + col);
	}
	
	public void setCard(CardWindow card, int index)
	{
		assert card != null;
		this.cards[index] = card;
		this.addCards();
	}
}
