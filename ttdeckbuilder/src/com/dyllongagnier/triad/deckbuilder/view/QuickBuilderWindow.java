package com.dyllongagnier.triad.deckbuilder.view;

import java.util.function.Consumer;

import javax.swing.JOptionPane;

public class QuickBuilderWindow extends DeckBuilderWindow
{
	private static final long serialVersionUID = 1L;
	
	private final Consumer<String[]> useBuiltDeck;
	
	public QuickBuilderWindow(Consumer<String[]> useBuiltDeck)
	{
		this.useBuiltDeck = useBuiltDeck;
		this.setVisible(true);
	}
	
	@Override
	protected void saveDeck()
	{
		try
		{
			this.useBuiltDeck.accept(this.deckPanel.getNames());
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Error, could not create deck. " + e.getMessage());
		}
		this.setVisible(false);
	}
}
