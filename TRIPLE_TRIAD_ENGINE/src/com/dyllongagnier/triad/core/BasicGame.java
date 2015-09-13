package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class BasicGame
{
	public static BoardState runGame(Player firstPlayer, Card[] selfCards, Card[] opponentCards, GameAgent selfAgent,
			GameAgent opponentAgent)
	{
		BoardState currentState = new BoardState.Builder()
			.setHand(Player.SELF, selfCards)
			.setHand(Player.OPPONENT, opponentCards)
			.build();
		Player currentPlayer = firstPlayer;
		for(int turn = 1; turn <= 9; turn++)
		{
			GameControls currentControls = new GameControls(currentState, currentPlayer);
			switch(currentPlayer)
			{
				case SELF:
					selfAgent.takeTurn(currentControls);
					break;
				case OPPONENT:
					opponentAgent.takeTurn(currentControls);
					break;
				default:
					throw new RuntimeException("Somehow, NONE became the current player.");
			}
			currentState = currentControls.getNextTurn();
			currentPlayer = currentPlayer.swapPlayer();
		}
		
		assert currentState.gameComplete();
		return currentState;
	}
}
