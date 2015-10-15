package com.dyllongagnier.triad.gui.view;

import java.awt.Container;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.Serializable;

import javax.swing.JOptionPane;

import com.dyllongagnier.triad.card.OrderedCard;
import com.dyllongagnier.triad.gui.controller.Players;

public class CardDropHandler implements DropTargetListener, Serializable
{
	private static final long serialVersionUID = 1L;
	private boolean canDrop = false;
	
	public void setCanDrop(boolean choice)
	{
		this.canDrop = choice;
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		if (dtde.isDataFlavorSupported(CardFlavor.cardFlavor))
			dtde.acceptDrag(DnDConstants.ACTION_MOVE);
		else
			dtde.rejectDrag();
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

	@Override
	public void dragExit(DropTargetEvent dte)
	{
	}

	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		if (dtde.isDataFlavorSupported(CardFlavor.cardFlavor) && this.canDrop)
		{
			Transferable trans = dtde.getTransferable();
			try
			{
				Object data = trans.getTransferData(CardFlavor.cardFlavor);
				CardWindow card =(CardWindow)data;
				DropTargetContext context = dtde.getDropTargetContext();
				CardCollection comp = (CardCollection)context.getComponent();
				Container parent = comp.getParent();
				Players.makeMove(CardCollection.getMoveFromIndex(comp.getCardMouseLocation(), new OrderedCard(card.card, card.cardLocation)));
				if (parent != null)
					parent.remove(card);
				comp.setCard(card, comp.getCardMouseLocation());
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				comp.validate();
				comp.repaint();
				
				dtde.dropComplete(true);
			}
			catch (Exception e)
			{
				dtde.rejectDrop();
				dtde.dropComplete(false);
				JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Invalid move.", "Invalid Move Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		else
		{
			dtde.rejectDrop();
			dtde.dropComplete(false);
			JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Invalid move.", "Invalid Move Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
