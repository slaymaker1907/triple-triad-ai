package com.dyllongagnier.triad.ai;

import java.util.List;
import java.util.Random;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.DefaultListener;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.TriadGame.PossibleMove;

public class FastSearchAI implements GameAgent
{
	@Override
	public void takeTurn(TriadGame controls)
	{
		List<PossibleMove> moves = controls.getValidMoves();
		PossibleMove bestMove = null;
		int currentBest = Integer.MIN_VALUE;
		for(PossibleMove move : moves)
		{
			TriadGame clone = controls.clone();
			clone.takeTurn(move.toPlay, move.row, move.col);
			int current = evaluateBoardState(clone.getCurrentState(), controls.getCurrentPlayer());
			if (current > currentBest)
			{
				currentBest = current;
				bestMove = move;
			}
		}
		controls.takeTurn(bestMove.toPlay, bestMove.row, bestMove.col);
	}
	
	private int evaluateBoardState(BoardState endState, Player player)
	{
		return endState.getCardsUnderPlayers().apply(player).length;
	}
	
	public static void main(String[] args)
	{
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.SELF, CardList.generateHand(Player.SELF, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.OPPONENT, CardList.generateHand(Player.OPPONENT, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		
		FastSearchAI ai1 = new FastSearchAI();
		FastSearchAI ai2 = new FastSearchAI();
		new TriadGame(getRandomPlayer(), builder, ai1, ai2, new DefaultListener()).startGame();
	}
	
	private static Random gen = new Random();
	public static Player getRandomPlayer()
	{
		if (gen.nextBoolean())
			return Player.SELF;
		else
			return Player.OPPONENT;
	}
	
	@Override
	public GameAgent clone()
	{
		return this;
	}
}
