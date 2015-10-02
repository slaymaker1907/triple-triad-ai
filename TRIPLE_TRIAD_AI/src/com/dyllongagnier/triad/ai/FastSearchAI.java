package com.dyllongagnier.triad.ai;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BasicGame;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.GameControls;
import com.dyllongagnier.triad.core.GameControls.PossibleMove;
import com.dyllongagnier.triad.core.functions.MoveValidator;

public class FastSearchAI implements GameAgent
{
	private final GameAgent testAgent;
	private final Consumer<BoardState> observer;
	
	public FastSearchAI(GameAgent testAgent, Consumer<BoardState> observer)
	{
		this.testAgent = testAgent;
		this.observer = observer;
	}
	
	public FastSearchAI(Consumer<BoardState> observer)
	{
		this.testAgent = new FastSearchAI(this, observer);
		this.observer = observer;
	}
	
	@Override
	public void takeTurn(GameControls controls)
	{
		this.observer.accept(controls.currentTurn);
		
		BoardState clone = controls.getCopyOfBoard();
		List<PossibleMove> moves = controls.getValidMoves();
		PossibleMove bestMove = null;
		int currentBest = Integer.MIN_VALUE;
		for(PossibleMove move : moves)
		{
			int current = possibleMoveMap(move, clone, controls.isOrder, controls.moveValidator, controls.currentPlayer);
			if (current > currentBest)
			{
				currentBest = current;
				bestMove = move;
			}
		}
		controls.playCard(bestMove.toPlay, bestMove.row, bestMove.col);
	}
	
	private int possibleMoveMap(PossibleMove move, BoardState currentState, boolean isOrder, MoveValidator validator, Player player)
	{
		currentState = currentState.playCard(player, move.toPlay, move.row, move.col);
		BoardState endState;
		switch(player)
		{
			case SELF:
				endState = BasicGame.runGame(player.swapPlayer(), currentState, validator, isOrder, this, this.testAgent);
				break;
			case OPPONENT:
				endState = BasicGame.runGame(player.swapPlayer(), currentState, validator, isOrder, this.testAgent, this);
				break;
			default:
				throw new RuntimeException("Can not be NONE.");
		}
		return this.evaluateBoardState(endState, player);
	}
	
	private int evaluateBoardState(BoardState endState, Player player)
	{
		assert endState.gameComplete();
		return endState.getCardsUnderPlayers().apply(player).length;
	}
	
	public static void main(String[] args)
	{
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.SELF, CardList.generateHand(Player.SELF, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.OPPONENT, CardList.generateHand(Player.OPPONENT, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		Consumer<BoardState> doNothing = (state) -> {};
		
		FastSearchAI ai1 = new FastSearchAI(doNothing);
		FastSearchAI ai2 = new FastSearchAI(doNothing);
		BasicGame.runGame(FastSearchAI::getRandomPlayer, builder, ai1, ai2);
	}
	
	private static Random gen = new Random();
	
	public static Player getRandomPlayer()
	{
		if (gen.nextBoolean())
			return Player.SELF;
		else
			return Player.OPPONENT;
	}
}
