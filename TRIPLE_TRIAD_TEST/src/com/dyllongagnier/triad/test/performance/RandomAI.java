package com.dyllongagnier.triad.test.performance;

import java.util.List;
import java.util.Random;

import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;

/**
 * This is a random AI for the purpose of testing.
 */
public class RandomAI implements GameAgent
{
	private Random gen = new Random();

	@Override
	public void takeTurn(TriadGame controls)
	{
		List<PossibleMove> moves = controls.getValidMoves();
		PossibleMove move = moves.get(gen.nextInt(moves.size()));
		controls.takeTurn(move.toPlay, move.row, move.col);
	}

	@Override
	public GameAgent clone()
	{
		return this;
	}
}
