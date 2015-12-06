package com.dyllongagnier.triad.test.performance;

import java.util.Random;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.OrderedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.DefaultListener;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;

public class BasicGamePerformance
{
	private static Random gen = new Random();

	public static Player getRandomPlayer()
	{
		if (gen.nextBoolean())
			return Player.BLUE;
		else
			return Player.RED;
	}

	public static void main(String[] args)
	{
		System.out.println(BasicGamePerformance.class.desiredAssertionStatus());
		BoardState.Builder builder = new BoardState.Builder();
		UndeployedCard[] cards = OrderedCard.convertToOrderedCard(CardList.generateHand(Player.BLUE, "Dodo",
				"Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.BLUE, cards);
		builder.setHand(Player.RED, cards);
		GameAgent ai = new RandomAI();
		for (int i = 0; i < 100_000; i++)
		{
			new TriadGame(BasicGamePerformance.getRandomPlayer(), builder, ai,
					ai, new DefaultListener()).startGame();
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 9000_000; i++)
		{
			new TriadGame(BasicGamePerformance.getRandomPlayer(), builder, ai,
					ai, new DefaultListener()).startGame();
		}
		System.out
				.println(900_000.0 / (System.currentTimeMillis() - start) * 1000.0);
	}
}
