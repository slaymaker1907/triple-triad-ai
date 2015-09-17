package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CardTest
{
	public final Card testCard = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);

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
	
	@Test
	public void testDeploy()
	{
		// Should be object equality.
		Card deployed = testCard.deploy();
		assertTrue(deployed == testCard);
	}
	
	@Test
	public void testToString()
	{
		String expected = ";BEASTMAN;5;1;2;3;4;NONE";
		assertEquals(expected, this.testCard.toString());
	}
	
	@Test
	public void testToString2()
	{
		String expected = "name;BEASTMAN;5;1;2;3;4;NONE";
		Card card = new Card(1,2,3,4, "name", Card.Type.BEASTMAN, 5);
		assertEquals(expected, card.toString());
	}
	
	@Test
	public void testClone()
	{
		// Should be object equality.
		Card clone = this.testCard.clone();
		assertTrue(clone == this.testCard);
	}
	
	@Test
	public void testIsVisible()
	{
		assertTrue(this.testCard.isVisible());
	}
	
	@Test
	public void testGetPossibleCards()
	{
		List<ProbCard> possibleCards = this.testCard.getPossibleCards();
		assertEquals(possibleCards.size(), 1);
		ProbCard card = possibleCards.get(0);
		assertEquals(1, card.probability, 0.001);
		assertTrue(card.card == this.testCard);
	}
	
	@Test
	public void testCompareToEqual()
	{
		Card other = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		assertEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualName()
	{
		Card other = new Card(1,2,3,4, "test", Card.Type.BEASTMAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualNorth()
	{
		Card other = new Card(2,2,3,4, "", Card.Type.BEASTMAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualEast()
	{
		Card other = new Card(1,1,3,4, "", Card.Type.BEASTMAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualSouth()
	{
		Card other = new Card(1,2,2,4, "", Card.Type.BEASTMAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualWest()
	{
		Card other = new Card(1,2,3,3, "", Card.Type.BEASTMAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualType()
	{
		Card other = new Card(1,2,3,4, "", Card.Type.GARLEAN, 5);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualRank()
	{
		Card other = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 2);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
	
	@Test
	public void testCompareNotEqualPlayer()
	{
		Card other = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5).setHoldingPlayer(Player.SELF);
		assertNotEquals(0, this.testCard.compareTo(other));
	}
}
