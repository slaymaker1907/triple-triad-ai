package com.dyllongagnier.triad.core.functions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.DeployedCard;

public class AscensionTransformTest
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testNoAscension()
	{
		DeployedCard dodo = new DeployedCard(CardList.getCard("Dodo"), 0, 1);
		DeployedCard newCard = AscensionTransform.noAscension(dodo, 5);
		assertEquals(dodo, newCard);
		assertTrue(dodo == newCard);
	}

	@Test
	public void testRegularAscension()
	{
		DeployedCard dodo = new DeployedCard(CardList.getCard("Dodo"), 0, 1);
		DeployedCard newCard = AscensionTransform.ascension(dodo, 5);
		DeployedCard expected = new DeployedCard(dodo.card.increaseAllStats(5),
				0, 1);
		assertEquals(expected, newCard);
	}

	@Test
	public void testDescension()
	{
		DeployedCard dodo = new DeployedCard(CardList.getCard("Dodo"), 0, 1);
		DeployedCard newCard = AscensionTransform.descension(dodo, 5);
		DeployedCard expected = new DeployedCard(
				dodo.card.increaseAllStats(-5), 0, 1);
		assertEquals(expected, newCard);
	}
}
