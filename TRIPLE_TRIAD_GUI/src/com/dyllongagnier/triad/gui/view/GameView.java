package com.dyllongagnier.triad.gui.view;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.PossibleMove;

public interface GameView
{
	public void setTurn(Player player); // This does permit Player.NONE.
	public void displayBoard(BoardState currentState, PossibleMove lastMove);
	public Player getFirstPlayer();
	
	public static GameView getGameView(boolean isServer)
	{
		return MainWindow.getMainWindow();
	}
}
