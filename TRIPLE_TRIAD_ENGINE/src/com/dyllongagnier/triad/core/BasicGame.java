package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class BasicGame
{
	/**
	 * This method runs a game with the given inputs.
	 * @param firstPlayer The first player to go.
	 * @param selfCards The cards for Player.SELF. Must contain 5 cards.
	 * @param opponentCards The cards for Player.OPPONENT. Must contain 5 cards.
	 * @param selfAgent The agent for self. The agent to use for playing Player.SELF.
	 * @param opponentAgent The agent for OPPONENT. The agent to use for playing Player.OPPONENT.
	 * @return The final BoardState of the game.
	 */
	public static BoardState runGame(Player firstPlayer, Card[] selfCards, Card[] opponentCards, GameAgent selfAgent,
			GameAgent opponentAgent)
	{
		if (firstPlayer == Player.NONE)
			throw new IllegalArgumentException();
		BoardState currentState = BasicGame.getInitialState(selfCards, opponentCards);
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
	
	/**
	 * Initializes a BoardState with the input cards 
	 * @param selfCards The cards of Player.SELF. Must be 5 cards.
	 * @param opponentCards The cards of Player.OPPONENT. Must be 5 cards.
	 * @return A board state with no cards played and the input cards in the appropriate hands.
	 */
	public static BoardState getInitialState(Card[] selfCards, Card[] opponentCards)
	{
		if (selfCards.length != 5 || opponentCards.length != 5)
			throw new IllegalArgumentException("selfCards.length:" + selfCards.length + " opponentCards.length:" + opponentCards.length);
		for(int i = 0; i < selfCards.length; i++)
			selfCards[i] = selfCards[i].setHoldingPlayer(Player.SELF);
		for(int i = 0; i < opponentCards.length; i++)
			opponentCards[i] = opponentCards[i].setHoldingPlayer(Player.OPPONENT);
		return new BoardState.Builder()
			.setHand(Player.SELF, selfCards)
			.setHand(Player.OPPONENT, opponentCards)
			.build();
	}
}
