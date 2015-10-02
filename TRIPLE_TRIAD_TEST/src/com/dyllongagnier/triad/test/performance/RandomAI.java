package com.dyllongagnier.triad.test.performance;

import java.util.List;
import java.util.Random;

import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.GameControls;
import com.dyllongagnier.triad.core.GameControls.PossibleMove;

/**
 * This is a random AI for the purpose of testing.
 */
public class RandomAI implements GameAgent
{
	private Random gen = new Random();
	
	@Override
	public void takeTurn(GameControls controls)
	{
		List<PossibleMove> moves = controls.getValidMoves();
		PossibleMove move = moves.get(gen.nextInt(moves.size()));
		controls.playCard(move.toPlay, move.row, move.col);
	}

}
