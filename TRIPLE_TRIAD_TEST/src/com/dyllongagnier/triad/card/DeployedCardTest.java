package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dyllongagnier.triad.card.DeployedCard.Direction;

public class DeployedCardTest
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testConstructor()
	{
		Card testCard = CardList.getCard("Dodo");
		DeployedCard test = new DeployedCard(testCard, 0, 1);
		assertTrue(testCard == test.card);
		assertEquals(0, test.row);
		assertEquals(1, test.col);
	}

	@Test
	public void testGetDirection1()
	{
		DeployedCard left = new DeployedCard(CardList.getCard("Dodo"), 0, 0);
		DeployedCard right = new DeployedCard(CardList.getCard("Dodo"), 0, 1);
		assertEquals(Direction.EAST, left.getDirectionOfOther(right));
		assertEquals(Direction.WEST, right.getDirectionOfOther(left));
	}

	@Test
	public void testGetDirection2()
	{
		DeployedCard upper = new DeployedCard(CardList.getCard("Dodo"), 0, 0);
		DeployedCard lower = new DeployedCard(CardList.getCard("Dodo"), 1, 0);
		assertEquals(Direction.SOUTH, upper.getDirectionOfOther(lower));
		assertEquals(Direction.NORTH, lower.getDirectionOfOther(upper));
	}

	@Test
	public void testGetDirection3()
	{
		DeployedCard middle = new DeployedCard(CardList.getCard("Dodo"), 1, 1);
		DeployedCard west = new DeployedCard(CardList.getCard("Dodo"), 1, 0);
		DeployedCard south = new DeployedCard(CardList.getCard("Dodo"), 2, 1);
		DeployedCard north = new DeployedCard(CardList.getCard("Dodo"), 0, 1);
		DeployedCard east = new DeployedCard(CardList.getCard("Dodo"), 1, 2);
		assertEquals(Direction.NORTH, middle.getDirectionOfOther(north));
		assertEquals(Direction.EAST, middle.getDirectionOfOther(east));
		assertEquals(Direction.SOUTH, middle.getDirectionOfOther(south));
		assertEquals(Direction.WEST, middle.getDirectionOfOther(west));
		assertEquals(Direction.SOUTH, north.getDirectionOfOther(middle));
		assertEquals(Direction.WEST, east.getDirectionOfOther(middle));
		assertEquals(Direction.NORTH, south.getDirectionOfOther(middle));
		assertEquals(Direction.EAST, west.getDirectionOfOther(middle));
		// Finish asserts.
	}

	@Test
	public void testSetPlayer()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard actual = new DeployedCard(dodo, 0, 1)
				.setPlayer(Player.BLUE);
		Card expected = dodo.setHoldingPlayer(Player.BLUE);
		assertEquals(expected, actual.card);
		assertEquals(0, actual.row);
		assertEquals(1, actual.col);
	}

	@Test
	public void testSetPlayerNoMutate()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard original = new DeployedCard(dodo, 0, 1);
		original.setPlayer(Player.BLUE);
		assertEquals(Player.NONE, original.card.holdingPlayer);
		assertTrue(dodo == original.card);
	}

	@Test
	public void cardAdjacentAbove()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard above = new DeployedCard(dodo, 0, 1);
		assertTrue(middle.cardAdjacent(above));
		assertTrue(above.cardAdjacent(middle));
	}

	@Test
	public void cardAdjacentLeft()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard left = new DeployedCard(dodo, 1, 0);
		assertTrue(middle.cardAdjacent(left));
		assertTrue(left.cardAdjacent(middle));
	}

	@Test
	public void cardNotAdjacentUpperLeft()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard upperLeft = new DeployedCard(dodo, 0, 0);
		assertFalse(middle.cardAdjacent(upperLeft));
		assertFalse(upperLeft.cardAdjacent(middle));
	}

	@Test
	public void cardNotAdjacentUpperRight()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard upperRight = new DeployedCard(dodo, 0, 2);
		assertFalse(middle.cardAdjacent(upperRight));
		assertFalse(upperRight.cardAdjacent(middle));
	}

	@Test
	public void cardNotAdjacentLowerLeft()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard lowerLeft = new DeployedCard(dodo, 2, 0);
		assertFalse(middle.cardAdjacent(lowerLeft));
		assertFalse(lowerLeft.cardAdjacent(middle));
	}

	@Test
	public void cardNotAdjacentLowerRight()
	{
		Card dodo = CardList.getCard("Dodo");
		DeployedCard middle = new DeployedCard(dodo, 1, 1);
		DeployedCard lowerRight = new DeployedCard(dodo, 2, 2);
		assertFalse(middle.cardAdjacent(lowerRight));
		assertFalse(lowerRight.cardAdjacent(middle));
	}

	@Test
	public void hashCodeTest()
	{
		Card test1 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		Card test2 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		DeployedCard dep1 = new DeployedCard(test1, 0, 1);
		DeployedCard dep2 = new DeployedCard(test2, 0, 1);
		assertEquals(dep1.hashCode(), dep2.hashCode());
	}

	@Test
	public void testEqualsDifferentCard()
	{
		Card test1 = new Card(1, 2, 3, 4, "", Card.Type.BEASTMAN, 5);
		Card test2 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		DeployedCard dep1 = new DeployedCard(test1, 0, 1);
		DeployedCard dep2 = new DeployedCard(test2, 0, 1);
		assertNotEquals(dep1, dep2);
	}

	@Test
	public void testEquals()
	{
		Card test1 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		Card test2 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		DeployedCard dep1 = new DeployedCard(test1, 0, 1);
		DeployedCard dep2 = new DeployedCard(test2, 0, 1);
		assertEquals(dep1, dep2);
	}

	@Test
	public void testEqualsDifferentRow()
	{
		Card test1 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		Card test2 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		DeployedCard dep1 = new DeployedCard(test1, 1, 1);
		DeployedCard dep2 = new DeployedCard(test2, 0, 1);
		assertNotEquals(dep1, dep2);
	}

	@Test
	public void testEqualsDifferentCol()
	{
		Card test1 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		Card test2 = new Card(1, 2, 3, 4, "name", Card.Type.BEASTMAN, 5);
		DeployedCard dep1 = new DeployedCard(test1, 0, 0);
		DeployedCard dep2 = new DeployedCard(test2, 0, 1);
		assertNotEquals(dep1, dep2);
	}
}
