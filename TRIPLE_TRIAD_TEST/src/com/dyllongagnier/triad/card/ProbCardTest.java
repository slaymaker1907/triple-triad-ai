package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProbCardTest 
{
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void probCardEquals()
	{
		Card testCard1 = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		Card testCard2 = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		ProbCard test1 = new ProbCard(testCard1, 0.7);
		ProbCard test2 = new ProbCard(testCard2, 0.7);
		assertEquals(test1, test2);
	}
	
	@Test
	public void probCardDiffCard()
	{
		Card testCard1 = new Card(1,2,3,4, "name", Card.Type.BEASTMAN, 5);
		Card testCard2 = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		ProbCard test1 = new ProbCard(testCard1, 0.7);
		ProbCard test2 = new ProbCard(testCard2, 0.7);
		assertNotEquals(test1, test2);
	}
	
	@Test
	public void probCardDiffProb()
	{
		Card testCard1 = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		Card testCard2 = new Card(1,2,3,4, "", Card.Type.BEASTMAN, 5);
		ProbCard test1 = new ProbCard(testCard1, 0.71);
		ProbCard test2 = new ProbCard(testCard2, 0.7);
		assertNotEquals(test1, test2);
	}
}
