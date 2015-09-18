package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import java.util.EnumMap;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testSwapPlayerSelf()
	{
		assertEquals(Player.OPPONENT, Player.SELF.swapPlayer());
	}

	@Test
	public void testSwapPlayerOpponent()
	{
		assertEquals(Player.SELF, Player.OPPONENT.swapPlayer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSwapPlayerNone()
	{
		Player.NONE.swapPlayer();
	}

	@Test
	public void randomPlayerGeneratesBothPlayers()
	{
		EnumMap<Player, Integer> playerBag = new EnumMap<>(Player.class);
		playerBag.put(Player.SELF, 0);
		playerBag.put(Player.OPPONENT, 0);
		playerBag.put(Player.NONE, 0);
		for (int i = 0; i < 100; i++)
		{
			playerBag.compute(Player.getRandomPlayer(),
					(player, count) -> count + 1);
		}

		assertEquals(0, playerBag.get(Player.NONE).intValue());
		assertNotEquals(0, playerBag.get(Player.SELF).intValue());
		assertNotEquals(0, playerBag.get(Player.OPPONENT).intValue());
	}
}
