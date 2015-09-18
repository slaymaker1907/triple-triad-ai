package com.dyllongagnier.triad.core;

import java.util.function.Function;
import java.util.function.Supplier;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

public class BasicGame {
	/**
	 * This method runs a game with the given inputs.
	 * 
	 * @param firstPlayerGen
	 *            A function to determine the first player to play.
	 * @param selfCards
	 *            The cards for Player.SELF. Must contain 5 cards.
	 * @param opponentCards
	 *            The cards for Player.OPPONENT. Must contain 5 cards.
	 * @param selfAgent
	 *            The agent for self. The agent to use for playing Player.SELF.
	 * @param opponentAgent
	 *            The agent for OPPONENT. The agent to use for playing
	 *            Player.OPPONENT.
	 * @param gameRules
	 *            The rules to use for playing this game.
	 * @return The final BoardState of the game.
	 */
	public static BoardState runGame(Supplier<Player> firstPlayerGen,
			UndeployedCard[] selfCards, UndeployedCard[] opponentCards,
			GameAgent selfAgent, GameAgent opponentAgent, Rules gameRules) {
		Player firstPlayer = firstPlayerGen.get();
		assert firstPlayer != Player.NONE;

		BoardState currentState = BasicGame.getInitialState(selfCards,
				opponentCards, gameRules);
		Player currentPlayer = firstPlayer;
		for (int turn = 1; turn <= 9; turn++) {
			GameControls currentControls = new GameControls(currentState,
					currentPlayer, gameRules.moveValidator);
			switch (currentPlayer) {
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

		if (gameRules.isSuddenDeath) {
			Player winner = currentState.getWinner();
			if (winner == Player.NONE) {
				Function<Player, UndeployedCard[]> cardFunc = currentState
						.getCardsUnderPlayers();
				return BasicGame.runGame(firstPlayerGen,
						cardFunc.apply(Player.SELF),
						cardFunc.apply(Player.SELF), selfAgent, opponentAgent,
						gameRules);
			}
		}

		return currentState;
	}

	/**
	 * Initializes a BoardState with the input cards
	 * 
	 * @param selfCards
	 *            The cards of Player.SELF. Must be 5 cards.
	 * @param opponentCards
	 *            The cards of Player.OPPONENT. Must be 5 cards.
	 * @return A board state with no cards played and the input cards in the
	 *         appropriate hands.
	 */
	public static BoardState getInitialState(UndeployedCard[] selfCards,
			UndeployedCard[] opponentCards, Rules gameRules) {
		assert selfCards.length == 5 && opponentCards.length == 5;
		return new BoardState.Builder(gameRules)
				.setHand(Player.SELF, selfCards)
				.setHand(Player.OPPONENT, opponentCards).build();
	}
}
