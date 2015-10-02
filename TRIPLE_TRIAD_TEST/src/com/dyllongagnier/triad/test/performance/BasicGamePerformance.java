package com.dyllongagnier.triad.test.performance;

import java.util.Random;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BasicGame;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.GameAgent;

public class BasicGamePerformance
{
	private static Random gen = new Random();
	
	public static Player getRandomPlayer()
	{
		if (gen.nextBoolean())
			return Player.SELF;
		else
			return Player.OPPONENT;
	}
	
	public static void main(String[] args)
	{
		System.out.println(BasicGamePerformance.class.desiredAssertionStatus());
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.SELF, CardList.generateHand(Player.SELF, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.OPPONENT, CardList.generateHand(Player.OPPONENT, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		GameAgent ai = new RandomAI();
		for(int i = 0; i < 100_000; i++)
		{
			BasicGame.runGame(BasicGamePerformance::getRandomPlayer, builder, ai, ai);
		}
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100_000; i++)
		{
			BasicGame.runGame(BasicGamePerformance::getRandomPlayer, builder, ai, ai);
		}
		System.out.println(900_000.0 / (System.currentTimeMillis() - start) * 1000.0);
	}
	
	

}
