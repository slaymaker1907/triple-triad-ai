package com.dyllongagnier.triad.deckbuilder.view;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextField;

import com.dyllongagnier.triad.deckbuilder.controller.CardGuesser;

public class DeckPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final JTextField[] inputCards = new JTextField[9];
	
	public DeckPanel() 
	{
		setLayout(new GridLayout(inputCards.length, 1, 0, 0));
		
		for(int i = 0; i < inputCards.length; i++)
		{
			inputCards[i] = new JTextField();
			this.add(inputCards[i]);
			inputCards[i].setColumns(10);
			inputCards[i].addKeyListener(new PredictiveTextListener(inputCards[i]));
		}
		this.clearNames();
	}
	
	public String[] getNames()
	{
		ArrayList<String> result = new ArrayList<>();
		for(JTextField textField : this.inputCards)
		{
			String text = textField.getText();
			if (!text.equals(""))
			{
				if (!CardGuesser.isValidCard(text))
					throw new IllegalArgumentException(text + " is not a card.");
				result.add(text);
			}
		}
		
		if (result.size() < 5)
			throw new IllegalArgumentException("A deck must contain five or more cards.");
		return result.toArray(new String[0]);
	}
	
	public void clearNames()
	{
		for(JTextField textField : this.inputCards)
			textField.setText("");
	}
	
	public static class PredictiveTextListener implements KeyListener
	{
		private final JTextField textField;
		private long lastPredict;
		
		public PredictiveTextListener(JTextField textField)
		{
			this.textField = textField;
			this.lastPredict = System.currentTimeMillis();
		}
		
		@Override
		public void keyTyped(KeyEvent e)
		{
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			String original = this.textField.getText();
			String match = CardGuesser.getClosestMatch(original);
			if (match != null && !original.equals("") && this.canPredict())
			{
				this.textField.setText(match);
				this.textField.select(original.length(), match.length());
			}
		}
		
		private boolean canPredict()
		{
			// Wait just a little bit to ensure a smooth user interaction.
			boolean result = System.currentTimeMillis() - this.lastPredict > 75;
			if (result)
				this.lastPredict = System.currentTimeMillis();
			return result;
		}
	}
}
