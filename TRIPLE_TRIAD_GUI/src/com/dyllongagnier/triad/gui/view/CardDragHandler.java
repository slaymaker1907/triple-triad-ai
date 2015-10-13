package com.dyllongagnier.triad.gui.view;

import java.awt.Cursor;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

public class CardDragHandler implements DragGestureListener, DragSourceListener
{
	private final CardWindow card;
	private CardCollection oldParent = null;
	private int location = -1;
	
	public CardDragHandler(CardWindow card)
	{
		this.card = card;
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde)
	{
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde)
	{
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde)
	{
	}

	@Override
	public void dragExit(DragSourceEvent dse)
	{
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		if (!dsde.getDropSuccess())
		{
			this.oldParent.setCard(this.card, this.location);
			this.oldParent.validate();
			this.oldParent.repaint();
		}
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		this.oldParent = (CardCollection)this.card.getParent();
		this.location = this.oldParent.getCardLocation(this.card);
		this.oldParent.setCard(new CardWindow(), this.location);
		this.oldParent.validate();
		this.oldParent.repaint();
		
		DragSource source = dge.getDragSource();
		source.startDrag(dge, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), this.card, this);
	}
}
