package com.dyllongagnier.triad.deckbuilder.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

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
	}
	
	public static class PredictiveTextListener implements KeyListener
	{
		private final JTextField textField;
		
		public PredictiveTextListener(JTextField textField)
		{
			this.textField = textField;
		}
		
		@Override
		public void keyTyped(KeyEvent e)
		{
			String original = this.textField.getText();
			String match = CardGuesser.getClosestMatch(original);
			if (match != null && !original.equals(""))
			{
				HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
				try
				{
					this.textField.getHighlighter().addHighlight(original.length(), match.length(), painter);
				} catch (BadLocationException e1)
				{
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
		}
	}
}
