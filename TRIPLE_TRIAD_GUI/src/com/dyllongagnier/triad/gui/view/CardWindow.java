package com.dyllongagnier.triad.gui.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.io.IOException;

import javax.swing.*;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class CardWindow extends JPanel implements Transferable
{
	private static final long serialVersionUID = 1L;
	
	private static int defaultSize = 100;
	
	public final Card card;
	private final DataFlavor[] flavors = new DataFlavor[]{CardFlavor.cardFlavor};
	@SuppressWarnings("unused")
	private final DragGestureRecognizer dgr;
	private final CardDragHandler handler;
	private final int ascensionBonus;
	
	public int cardLocation;

	public CardWindow(Card card, int ascensionBonus)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.ascensionBonus = ascensionBonus;
		this.card = card;
		if (card != null)
			this.init();
		handler = new CardDragHandler(this);
		dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, handler);
	}
	
	public void setCanDrag(boolean canDrag)
	{
		this.handler.setCanDrag(canDrag);
	}
	
	public CardWindow()
	{
		ascensionBonus = 0;
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.card = null;
		handler = new CardDragHandler(this);
		dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, handler);
	}
	
	private void init()
	{
		this.setLayout(new GridLayout(3, 3));
		this.add("upperleft", new JLabel());
		this.add("uppermid", CardWindow.makeLabel(this.card.north));
		this.add("upperright", this.getAscensionDisplay());
		this.add("midleft", CardWindow.makeLabel(this.card.west));
		this.add("center", CardWindow.makeLabel(this.card.cardType));
		this.add("midright", CardWindow.makeLabel(this.card.east));
		this.add("lowerleft", new JLabel());
		this.add("lowermid", CardWindow.makeLabel(this.card.south));
		this.add("lowerright", new JLabel());
		this.setColor(this.card.holdingPlayer);
	}
	
	private JLabel getAscensionDisplay()
	{
		int bonus = Math.abs(this.ascensionBonus);
		String text;
		if (this.ascensionBonus < 0)
			text = "-" + bonus;
		else if (this.ascensionBonus > 0)
			text = "+" + bonus;
		else
			text = "";
		JLabel result = new JLabel(text);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		return result;
	}
	
	private void setColor(Player player)
	{
		Color toSet;
		switch(player)
		{
			case SELF:
				// Blue
				toSet = new Color(200, 200, 255);
				break;
			case OPPONENT:
				// Red
				toSet = new Color(255, 200, 200);
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
	
	public static String getAscensionIcon(Card.Type type)
	{
		switch(type)
		{
			case BEASTMAN:
				return "B";
			case GARLEAN:
				return "G";
			case PRIMAL:
				return "P";
			case SCION:
				return "S";
			default:
				return "";
		}
	}
	
	private static JLabel makeLabel(Card.Type type)
	{
		JLabel result = new JLabel(CardWindow.getAscensionIcon(type));
		result.setHorizontalAlignment(SwingConstants.CENTER);
		return result;
	}
	
	public static int getDefaultSize()
	{
		return CardWindow.defaultSize;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return this.flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		for(DataFlavor flav : this.getTransferDataFlavors())
			if (flav.equals(flavor))
				return true;
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
	{
		if (this.isDataFlavorSupported(flavor))
			return this;
		else
			throw new UnsupportedFlavorException(flavor);
	}
	
	public void setLastPlayedCard(boolean lastPlayed)
	{
		if (lastPlayed)
			this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		else
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
}
