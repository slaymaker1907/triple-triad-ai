package com.dyllongagnier.triad.ai;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.DefaultListener;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;

public class FastSearchAI implements GameAgent
{
	@Override
	public void takeTurn(TriadGame controls)
	{
		List<PossibleMove> moves = controls.getValidMoves();
		NodeFutures futures = new NodeFutures((turn)->controls.takeTurn(turn.toPlay, turn.row, turn.col), moves.size());
		for(PossibleMove move : moves)
		{
			futures.addNode(move, fullEvaluateBoard(controls, move), evaluateBoardState(controls, move));
		}
	}
	
	private static Supplier<Integer> evaluateBoardState(TriadGame toClone, PossibleMove move)
	{
		return () ->
		{
			TriadGame clone = toClone.clone();
			Player currentPlayer = clone.getCurrentPlayer();
			BoardState playedState = clone.getCurrentState().playCard(clone.getCurrentPlayer(), move.toPlay, move.row, move.col);
			return getCardsUnderPlayer(playedState, currentPlayer);
		};
	}
	
	private static int getCardsUnderPlayer(BoardState state, Player player)
	{
		return state.getCardsUnderPlayers().apply(player).length;
	}
	
	private static Supplier<Integer> fullEvaluateBoard(TriadGame toClone, PossibleMove move)
	{
		return () ->
		{
			TriadGame clone = toClone.clone();
			Player currentPlayer = clone.getCurrentPlayer();
			clone.takeTurn(move.toPlay, move.row, move.col);
			return getCardsUnderPlayer(clone.getCurrentState(), currentPlayer);
		};
	}
	
	public static void main(String[] args)
	{
		EvaluationQueue.setThreadCount(8);
		EvaluationQueue.beginProcessing();
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.SELF, CardList.generateHand(Player.SELF, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.OPPONENT, CardList.generateHand(Player.OPPONENT, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		
		FastSearchAI ai1 = new FastSearchAI();
		FastSearchAI ai2 = new FastSearchAI();
		new TriadGame(getRandomPlayer(), builder, ai1, ai2, new DefaultListener()).startGame();
		EvaluationQueue.stopProcessing();
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
