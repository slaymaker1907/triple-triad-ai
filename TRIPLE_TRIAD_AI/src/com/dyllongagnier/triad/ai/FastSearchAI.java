package com.dyllongagnier.triad.ai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.GameListener;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.core.GameAgent;

public class FastSearchAI implements GameAgent
{
	
	private class GameRunner implements Runnable
	{
		private final PossibleMove move;
		private final TriadGame clone;
		private final int heuristic;
		private final EndGameReporter listener;
		
		public GameRunner(PossibleMove move, TriadGame currentState, EndGameReporter listener)
		{
			this.move = move;
			this.clone = currentState.clone(listener);
			this.heuristic = this.calculateHeuristic();
			this.listener = listener;
		}
		
		private int calculateHeuristic()
		{
			TriadGame newClone = this.clone.clone();
			return newClone.getCurrentState().playCard(newClone.getCurrentPlayer(), this.move.toPlay, this.move.row, this.move.col)
					.getCardsUnderPlayers().apply(newClone.getCurrentPlayer()).length;
		}
		
		@Override
		public void run()
		{
			if (finishQuickly)
				this.listener.executeAfter.accept(this.heuristic);
			else
				this.clone.takeTurn(this.move.toPlay,this.move.row, this.move.col);
		}
	}
	
	public static int compareGameRunner(Runnable r1, Runnable r2)
	{
		// Casting is safe since this is an internal class.
		GameRunner g1, g2;
		g1 = (GameRunner)r1;
		g2 = (GameRunner)r2;
		return Integer.compare(g1.heuristic, g2.heuristic);
	}
	
	private static class EndGameReporter implements GameListener
	{
		public final Consumer<Integer> executeAfter;
		private final Player currentPlayer;
		
		public EndGameReporter(Consumer<Integer> executeAfter, Player currentPlayer)
		{
			this.executeAfter = executeAfter;
			this.currentPlayer = currentPlayer;
		}

		@Override
		public void gameChanged(TriadGame changedGame)
		{
		}

		@Override
		public void gameComplete(TriadGame finalState)
		{
			this.executeAfter.accept(finalState.getCurrentState().getCardsUnderPlayers().apply(currentPlayer).length);
		}
	}
	
	private class MoveExecutor
	{
		private final HashMap<PossibleMove, Integer> moveValue = new HashMap<>();
		private final TriadGame game;
		private int size; // Only one thread will ever be adding possible moves.
		
		public MoveExecutor(TriadGame game)
		{
			this.game = game;
		}
		
		public GameRunner addPossibleMove(PossibleMove move)
		{
			this.size++;
			EndGameReporter listener = new EndGameReporter(this.getConsumer(move), this.game.getCurrentPlayer());
			return new GameRunner(move, this.game, listener);
		}
		
		private Consumer<Integer> getConsumer(PossibleMove move)
		{
			return (val) ->
			{
				synchronized(moveValue)
				{
					moveValue.put(move, val);
					if (moveValue.size() >= size)
					{
						this.makeMove();
					}
				}
			};
		}
		
		private void makeMove()
		{
			PossibleMove bestMove = this.getBestMove();
			this.game.takeTurn(bestMove.toPlay, bestMove.row, bestMove.col);
		}
		
		private PossibleMove getBestMove()
		{
			// This should be guaranteed to be thread safe since it is invoked upon the last runners completion.
			int bestVal = Integer.MIN_VALUE;
			PossibleMove bestMove = null;
			for (PossibleMove move : this.moveValue.keySet())
			{
				int current = this.moveValue.get(move);
				if (current > bestVal)
				{
					bestMove = move;
					bestVal = current;
				}
			}
			
			return bestMove;
		}
	}

	private final ThreadPoolExecutor executor;
	private volatile boolean finishQuickly = false;
	
	public FastSearchAI(int cores)
	{
		Comparator<Runnable> comp = FastSearchAI::compareGameRunner;
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<>(11, comp);
		this.executor = new ThreadPoolExecutor(cores, cores, Long.MAX_VALUE, TimeUnit.DAYS, queue);

	}
	
	public void setMoveTimeout(long timeout)
	{
		this.finishQuickly = false;
		Thread watcher = new Thread(() ->
		{
			try
			{
				Thread.sleep(timeout);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			this.finishQuickly = true;
		});
		watcher.setDaemon(true);
		watcher.start();
	}
	
	@Override
	public void takeTurn(TriadGame controls)
	{
		List<PossibleMove> moves = controls.getValidMoves();
		MoveExecutor exec = new MoveExecutor(controls);
		for(PossibleMove move : moves)
		{
			this.executor.execute(exec.addPossibleMove(move));
		}
	}
	
	@Override
	public GameAgent clone()
	{
		return this;
	}
}
