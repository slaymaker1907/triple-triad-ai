package com.dyllongagnier.triad.gui.view;

import java.awt.datatransfer.DataFlavor;

public class CardFlavor extends DataFlavor
{
    public static final CardFlavor cardFlavor = new CardFlavor();
	
	public CardFlavor()
	{
		super(CardWindow.class, null);
	}
}
