package com.dyllongagnier.triad.test.performance;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.DefaultListener;
import com.dyllongagnier.triad.core.GameAgent;
import com.dyllongagnier.triad.core.TriadGame;

public class TriadGameCloneSpeed
{

	public static void main(String[] args)
	{
		System.out.println(BasicGamePerformance.class.desiredAssertionStatus());
		BoardState.Builder builder = new BoardState.Builder();
		builder.setHand(Player.SELF, CardList.generateHand(Player.SELF, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		builder.setHand(Player.OPPONENT, CardList.generateHand(Player.OPPONENT, "Dodo", "Gaelicat", "Tonberry", "Sabotender", "Spriggan"));
		GameAgent ai = new RandomAI();
		TriadGame mainGame = new TriadGame(BasicGamePerformance.getRandomPlayer(), builder, ai, ai, new DefaultListener()).clone();
		for(int i = 0; i < 1_000_000_000; i++)
		{
			mainGame.clone();
		}
		long start = System.currentTimeMillis();
		for(int i = 0; i < 1_000_000_000; i++)
		{
			mainGame.clone();
		}
		System.out.println(900_000.0 / (System.currentTimeMillis() - start) * 1000.0);
	}

}
