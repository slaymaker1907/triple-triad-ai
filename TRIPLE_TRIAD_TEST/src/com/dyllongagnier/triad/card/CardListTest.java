package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class CardListTest
{
	@Before
	public void setUp() throws Exception
	{
	}
	
	public static void assertCardEqual(Card actual, Card expected)
	{
		assertEquals(actual, expected);
		assertEquals(actual.cardRarity, expected.cardRarity);
		assertEquals(actual.cardType, expected.cardType);
		assertEquals(actual.east, expected.east);
		assertEquals(actual.north, expected.north);
		assertEquals(actual.south, expected.south);
		assertEquals(actual.west, expected.west);
	}

	@Test
	public void TestSingleCardLookup()
	{
		Card retrieved = CardList.getCard("Dodo");
		assertNotNull(retrieved);
		Card expected = new Card(4, 2, 3, 4, "Dodo", Card.Type.NONE, 1);
		assertCardEqual(retrieved, expected);
	}

	@Test
	public void TestCardTypeLookup()
	{
		Card retrieved = CardList.getCard("Bahamut");
		assertNotNull(retrieved);
		Card expected = new Card(9, 5, 9, 6, "Bahamut", Card.Type.PRIMAL, 5);
		assertCardEqual(retrieved, expected);
	}
	
	@Test
	public void VerifyCardsUnique()
	{
		Collection<Card> allCards = CardList.getAllCards();
		HashSet<String> cards = new HashSet<String>();
		for(Card card : allCards)
		{
			String name = card.name;
			assertFalse(cards.contains(name));
			cards.add(name);
		}
	}
}
