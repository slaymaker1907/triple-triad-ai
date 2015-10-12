package com.dyllongagnier.triad.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class CardWindow extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static int defaultSize = 100;
	
	private final Card card;

	public CardWindow(Card card)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.card = card;
		if (card != null)
			this.init();
	}
	
	public CardWindow()
	{
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.card = null;
	}
	
	private void init()
	{
		this.setSize(defaultSize, defaultSize);
		this.setLayout(new GridLayout(3, 3));
		this.add("upperleft", new JLabel());
		this.add("uppermid", CardWindow.makeLabel(this.card.north));
		this.add("upperright", new JLabel());
		this.add("midleft", CardWindow.makeLabel(this.card.west));
		this.add("center", new JLabel());
		this.add("midright", CardWindow.makeLabel(this.card.east));
		this.add("lowerleft", new JLabel());
		this.add("lowermid", CardWindow.makeLabel(this.card.south));
		this.add("lowerright", new JLabel());
		this.setColor(this.card.holdingPlayer);
		Dimension square = new Dimension(CardWindow.defaultSize, CardWindow.defaultSize);
		this.setMaximumSize(square);
		this.setSize(square);
		this.setMinimumSize(square);
	}
	
	private void setColor(Player player)
	{
		Color toSet;
		switch(player)
		{
			case SELF:
				// Red
				toSet = new Color(255, 200, 200);
				break;
			case OPPONENT:
				// Blue
				toSet = new Color(200, 200, 255);
				break;
			default:
				// Grey
				toSet = new Color(204, 204, 204);
				break;
		}
		this.setBackground(toSet);
	}
	
	private static JLabel makeLabel(int num)
	{
		String label;
		if (num == 10)
			label = "A";
		else
			label = String.valueOf(num);
		JLabel result = new JLabel(label);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		return result;
	}
	
	public static int getDefaultSize()
	{
		return CardWindow.defaultSize;
	}
}
