package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest
{
	public static final Card testCard = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);

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
	
	@Test
	public void testIncreaseStats()
	{
		Card newCard = testCard.increaseAllStats(1);
		assertEquals(newCard.cardRarity, 5);
		assertEquals(newCard.north, 2);
		assertEquals(newCard.east, 3);
		assertEquals(newCard.south, 4);
		assertEquals(newCard.west, 5);
		assertEquals(newCard.name, "");
		assertEquals(newCard.cardType, Card.Type.BEASTMAN);
		assertEquals(newCard.holdingPlayer, Player.NONE);
	}
	
	@Test
	public void testIncreaseBeyondMax()
	{
		Card newCard = testCard.increaseAllStats(7);
		assertEquals(newCard.cardRarity, 5);
		assertEquals(newCard.north, 8);
		assertEquals(newCard.east, 9);
		assertEquals(newCard.south, 10);
		assertEquals(newCard.west, 10);
		assertEquals(newCard.name, "");
		assertEquals(newCard.cardType, Card.Type.BEASTMAN);
		assertEquals(newCard.holdingPlayer, Player.NONE);
	}
	
	@Test
	public void testIncreaseStatsNeg()
	{
		Card newCard = testCard.increaseAllStats(-1);
		assertEquals(newCard.cardRarity, 5);
		assertEquals(newCard.north, 1);
		assertEquals(newCard.east, 1);
		assertEquals(newCard.south, 2);
		assertEquals(newCard.west, 3);
		assertEquals(newCard.name, "");
		assertEquals(newCard.cardType, Card.Type.BEASTMAN);
		assertEquals(newCard.holdingPlayer, Player.NONE);
	}
	
	@Test
	public void testEquals()
	{
		Card testCopy = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		assertTrue(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsName()
	{
		Card testCopy = new Card(1,2,3,4, "a", Card.Type.BEASTMAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsNorth()
	{
		Card testCopy = new Card(2,2,3,4, "", Card.Type.BEASTMAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsEast()
	{
		Card testCopy = new Card(1,1,3,4, "", Card.Type.BEASTMAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsSouth()
	{
		Card testCopy = new Card(1,2,2,4, "", Card.Type.BEASTMAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsWest()
	{
		Card testCopy = new Card(1,2,3,3, "", Card.Type.BEASTMAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsType()
	{
		Card testCopy = new Card(1,2,3,4, "", Card.Type.GARLEAN, 5);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsRarity()
	{
		Card testCopy = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 1);
		assertFalse(testCopy.equals(testCard));
	}
	
	@Test
	public void testNotEqualsPlayer()
	{
		Card testCopy = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5).setHoldingPlayer(Player.SELF);
		assertFalse(testCopy.equals(testCard));
	}
}
