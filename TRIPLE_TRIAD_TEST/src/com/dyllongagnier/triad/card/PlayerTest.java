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
		assertEquals(Player.RED, Player.BLUE.swapPlayer());
	}

	@Test
	public void testSwapPlayerOpponent()
	{
		assertEquals(Player.BLUE, Player.RED.swapPlayer());
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
		playerBag.put(Player.BLUE, 0);
		playerBag.put(Player.RED, 0);
		playerBag.put(Player.NONE, 0);
		for (int i = 0; i < 100; i++)
		{
			playerBag.compute(Player.getRandomPlayer(),
					(player, count) -> count + 1);
		}

		assertEquals(0, playerBag.get(Player.NONE).intValue());
		assertNotEquals(0, playerBag.get(Player.BLUE).intValue());
		assertNotEquals(0, playerBag.get(Player.RED).intValue());
	}
}
