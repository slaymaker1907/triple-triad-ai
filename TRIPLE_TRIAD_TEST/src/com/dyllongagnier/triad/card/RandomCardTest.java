package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RandomCardTest
{	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void constructorNoExceptOne()
	{
		Card dodo = CardList.getCard("Dodo");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		new RandomCard(cards);
	}
	
	@Test
	public void constructorNoExceptMultiple()
	{
		new RandomCard(CardList.getAllCards());
	}
	
	@Test
	public void testGetAllPossibleCards()
	{
		Card dodo = CardList.getCard("Dodo");
		Card gael = CardList.getCard("Gaelicat");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		cards.add(gael);
		RandomCard card = new RandomCard(cards);
		List<ProbCard> probCards = card.getPossibleCards();
		
		ProbCard expected1 = new ProbCard(dodo, 1.0 / 2);
		ProbCard expected2 = new ProbCard(dodo, 1.0/2);
		assertTrue(probCards.contains(expected1));
		assertTrue(probCards.contains(expected2));
	}
	
	@Test
	public void testGetAllPossibleCards2()
	{
		Collection<Card> allCards = CardList.getAllCards();
		RandomCard randCard = new RandomCard(allCards);
		double probability = 1.0 / allCards.size();
		List<ProbCard> probCards = randCard.getPossibleCards();
		assertEquals(allCards.size(), probCards.size());
		
		for(Card card : allCards)
		{
			assertTrue(probCards.contains(new ProbCard(card, probability)));
		}
	}
	
	@Test
	public void cantMutateInitialList()
	{
		Card dodo = CardList.getCard("Dodo");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		RandomCard randCard = new RandomCard(cards);
		cards.clear();
		assertEquals(1, randCard.getPossibleCards().size());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void cantMutateReturnedList()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		List<ProbCard> initList = randCard.getPossibleCards();
		initList.clear();
	}
	
	@Test
	public void testBasicDeploy()
	{
		Card dodo = CardList.getCard("Dodo");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		RandomCard randCard = new RandomCard(cards);
		assertEquals(dodo, randCard.deploy());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeployExcept()
	{
		Card dodo = CardList.getCard("Dodo");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		RandomCard randCard = new RandomCard(cards);
		randCard.deploy();
		randCard.deploy();
	}
	
	@Test
	public void testMultipleDeploy()
	{
		Card dodo = CardList.getCard("Dodo");
		Card gael = CardList.getCard("Gaelicat");
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(dodo);
		cards.add(gael);
		RandomCard randCard = new RandomCard(cards);
		
		boolean seenGael, seenDodo;
		seenGael = seenDodo = false;
		for(int i = 0; i < 2; i++)
		{
			Card card = randCard.deploy();
			if (card.equals(dodo))
				seenDodo = true;
			else if (card.equals(gael))
				seenGael = true;
		}
		
		assertTrue(seenDodo);
		assertTrue(seenGael);
	}
	
	@Test
	public void testCloneNotSameOb()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		RandomCard clone = randCard.clone();
		assertFalse(randCard == clone);
		assertFalse(randCard.getPossibleCards() == clone.getPossibleCards());
	}
	
	@Test
	public void testCloneMultipleCalls()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		RandomCard clone1 = randCard.clone();
		RandomCard clone2 = randCard.clone();
		assertTrue(clone1 == clone2);
	}
	
	@Test
	public void testCloneSamePossibleCards()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		RandomCard clone = randCard.clone();
		assertEquals(randCard.getPossibleCards(), clone.getPossibleCards());
	}
	
	@Test
	public void testNotEquals()
	{
		RandomCard randCard1 = new RandomCard(CardList.getAllCards());	
		RandomCard randCard2 = new RandomCard(CardList.getAllCards());
		assertNotEquals(randCard1, randCard2);
	}
	
	@Test
	public void testEquals()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		assertEquals(randCard, randCard);
	}
	
	@Test
	public void testSameHashCode()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());	
		int firsthash = randCard.hashCode();
		assertEquals(firsthash, randCard.hashCode());
	}
	
	@Test
	public void testUniqueHashCode()
	{
		RandomCard randCard1 = new RandomCard(CardList.getAllCards());	
		RandomCard randCard2 = new RandomCard(CardList.getAllCards());
		assertNotEquals(randCard1.hashCode(), randCard2.hashCode());
	}
	
	@Test
	public void testSameCompareTo()
	{
		RandomCard randCard = new RandomCard(CardList.getAllCards());
		assertEquals(0, randCard.compareTo(randCard));
	}
	
	@Test
	public void testNotSameCompareTo()
	{
		RandomCard randCard1 = new RandomCard(CardList.getAllCards());	
		RandomCard randCard2 = new RandomCard(CardList.getAllCards());
		assertNotEquals(0, randCard2.compareTo(randCard1));
		assertNotEquals(0, randCard1.compareTo(randCard2));
	}
	
	@Test
	public void testNotVisible()
	{
		RandomCard card = new RandomCard(CardList.getAllCards());
		assertFalse(card.isVisible());
	}
}
