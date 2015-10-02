package com.dyllongagnier.triad.core;

import java.util.function.Supplier;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.functions.MoveValidator;

public class BasicGame
{
	/**
	 * This method runs a game with the given inputs.
	 * 
	 * @param firstPlayerGen
	 *            A function to determine the first player to play.
	 * @param gameBuilder
	 * 			  The game builder to use for this game.
	 * @param selfAgent
	 *            The agent for self. The agent to use for playing Player.SELF.
	 * @param opponentAgent
	 *            The agent for OPPONENT. The agent to use for playing
	 *            Player.OPPONENT.
	 * @return The final BoardState of the game.
	 */
	public static BoardState runGame(Supplier<Player> firstPlayerGen,
			BoardState.Builder gameBuilder,
			GameAgent selfAgent, GameAgent opponentAgent)
	{
		Player firstPlayer = firstPlayerGen.get();
		assert firstPlayer != Player.NONE;

		BoardState currentState = gameBuilder.build();
		Player currentPlayer = firstPlayer;
		for (int turn = 1; turn <= 9; turn++)
		{
			GameControls currentControls = new GameControls(currentState,
					currentPlayer, gameBuilder.getMoveValidator(), gameBuilder.getIsOrder());
			switch (currentPlayer)
			{
				case SELF:
					selfAgent.takeTurn(currentControls);
					break;
				case OPPONENT:
					opponentAgent.takeTurn(currentControls);
					break;
				default:
					throw new RuntimeException(
							"Somehow, NONE became the current player.");
			}
			currentState = currentControls.getNextTurn();
			currentPlayer = currentPlayer.swapPlayer();
		}

		assert currentState.gameComplete();

		if (gameBuilder.isSuddenDeath)
		{
			Player winner = currentState.getWinner();
			if (winner == Player.NONE)
			{
				return BasicGame.runGame(firstPlayerGen, gameBuilder, selfAgent, opponentAgent);
			}
		}

		return currentState;
	}
	
	/**
	 * This method runs a game with the given inputs.
	 * 
	 * @param firstPlayer
	 *            The first player.
	 * @param gameBuilder
	 * 			  The game builder to use for this game.
	 * @param selfAgent
	 *            The agent for self. The agent to use for playing Player.SELF.
	 * @param opponentAgent
	 *            The agent for OPPONENT. The agent to use for playing
	 *            Player.OPPONENT.
	 * @return The final BoardState of the game.
	 */
	public static BoardState runGame(Player firstPlayer,
			BoardState currentState, MoveValidator validator, boolean isOrder,
			GameAgent selfAgent, GameAgent opponentAgent)
	{
		assert firstPlayer != Player.NONE;

		Player currentPlayer = firstPlayer;
		int turnCount = 11 - (currentState.getHand(Player.SELF).size() + currentState.getHand(Player.OPPONENT).size());
		for (int turn = turnCount; turn <= 9; turn++)
		{
			GameControls currentControls = new GameControls(currentState,
					currentPlayer, validator, isOrder);
			switch (currentPlayer)
			{
				case SELF:
					selfAgent.takeTurn(currentControls);
					break;
				case OPPONENT:
					opponentAgent.takeTurn(currentControls);
					break;
				default:
					throw new RuntimeException(
							"Somehow, NONE became the current player.");
			}
			currentState = currentControls.getNextTurn();
			currentPlayer = currentPlayer.swapPlayer();
		}

		assert currentState.gameComplete();
		
		return currentState;
	}
}
