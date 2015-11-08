package com.dyllongagnier.triad.test.performance;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.DefaultListener;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.TriadGame;

public class BoardStateGetWinner
{

	public static void main(String[] args)
	{
		System.out.println(BasicGamePerformance.class.desiredAssertionStatus());
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.BLUE, CardList.generateHand(Player.BLUE, "Dodo",
				"Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.RED, CardList.generateHand(Player.RED,
				"Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		GameAgent ai = new RandomAI();
		TriadGame game = new TriadGame(BasicGamePerformance.getRandomPlayer(),
				builder, ai, ai, new DefaultListener());
		game.startGame();
		BoardState state = game.getCurrentState();
		for (int i = 0; i < 100_000; i++)
		{
			state.getWinner();
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100_000; i++)
		{
			state.getWinner();
		}
		System.out
				.println(900_000.0 / (System.currentTimeMillis() - start) * 1000.0);
	}
}
