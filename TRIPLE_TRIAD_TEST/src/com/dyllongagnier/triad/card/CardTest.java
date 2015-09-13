package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void assertFieldsInOrder()
	{
		Card test = new Card(1,2,3,4,"",Card.Type.BEASTMAN, 5);
		assertEquals(test.cardRarity, 5);
		assertEquals(test.north, 1);
		assertEquals(test.east, 2);
		assertEquals(test.south, 3);
		assertEquals(test.west, 4);
		assertEquals(test.name, "");
		assertEquals(test.cardType, Card.Type.BEASTMAN);
		assertEquals(test.holdingPlayer, Player.NONE);
	}
	
	@Test
	public void assertFieldsInOrder2()
	{
		Card test = new Card(1,2,3,4,"",Card.Type.BEASTMAN, 5, Player.OPPONENT);
		assertEquals(test.cardRarity, 5);
		assertEquals(test.north, 1);
		assertEquals(test.east, 2);
		assertEquals(test.south, 3);
		assertEquals(test.west, 4);
		assertEquals(test.name, "");
		assertEquals(test.cardType, Card.Type.BEASTMAN);
		assertEquals(test.holdingPlayer, Player.OPPONENT);
	}
	
	@Test
	public void setHoldingPlayerTest()
	{
		Card test = new Card(1,2,3,4,"",Card.Type.BEASTMAN, 5).setHoldingPlayer(Player.OPPONENT);
		assertEquals(test.cardRarity, 5);
		assertEquals(test.north, 1);
		assertEquals(test.east, 2);
		assertEquals(test.south, 3);
		assertEquals(test.west, 4);
		assertEquals(test.name, "");
		assertEquals(test.cardType, Card.Type.BEASTMAN);
		assertEquals(test.holdingPlayer, Player.OPPONENT);
	}
}
