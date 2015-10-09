package com.dyllongagnier.triad.card;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dyllongagnier.triad.card.HandFactory;

public class HandFactoryTest
{
	private static final String threeOpenExampleName = "resources"
			+ File.separator + "ThreeOpenExample.json";
	private UndeployedCard[] threeOpenExample;

	@Before
	public void setUp() throws Exception
	{
		threeOpenExample = HandFactory.getDeck(Player.SELF,
				threeOpenExampleName);
	}

	@Test
	public void testHandSize()
	{
		assertEquals(5, threeOpenExample.length);
	}

	@Test
	public void threeVisibleCards()
	{
		int count = 0;
		for (UndeployedCard card : threeOpenExample)
		{
			if (card.isVisible())
				count++;
		}
		assertEquals(3, count);
	}

	@Test
	public void containsRightVisibleCards()
	{
		boolean seenGael, seenDodo, seenTonberry;
		seenGael = seenDodo = seenTonberry = false;
		Card gael = CardList.getCard("Gaelicat").setHoldingPlayer(Player.SELF);
		Card dodo = CardList.getCard("Dodo").setHoldingPlayer(Player.SELF);
		Card tonberry = CardList.getCard("Tonberry").setHoldingPlayer(
				Player.SELF);

		for (UndeployedCard undepCard : threeOpenExample)
		{
			if (undepCard.isVisible())
			{
				Card card = undepCard.deploy();
				if (card.equals(gael))
					seenGael = true;
				else if (card.equals(dodo))
					seenDodo = true;
				else if (card.equals(tonberry))
					seenTonberry = true;
			}
		}

		assertTrue(seenGael);
		assertTrue(seenTonberry);
		assertTrue(seenDodo);
	}

	@Test
	public void hiddenCardsEqual()
	{
		UndeployedCard hiddenCard = null;
		for (UndeployedCard undepCard : threeOpenExample)
		{
			if (!undepCard.isVisible())
			{
				if (hiddenCard == null)
					hiddenCard = undepCard;
				else
					assertTrue(hiddenCard == undepCard);
			}
		}
	}

	@Test
	public void testHiddenCardsCorrect()
	{
		ArrayList<Card> expectedCards = new ArrayList<>();
		expectedCards.add(CardList.getCard("Sabotender"));
		expectedCards.add(CardList.getCard("Spriggan"));
		expectedCards.add(CardList.getCard("Pudding"));
		expectedCards.add(CardList.getCard("Bomb"));
		expectedCards.add(CardList.getCard("Mandragora"));

		for (UndeployedCard undepCard : threeOpenExample)
		{
			if (!undepCard.isVisible())
			{
				undepCard.getPossibleCards().containsAll(expectedCards);
			}
		}
	}

	@Test
	public void testMinimumHidden() throws Exception
	{
		// This should execute without an exception.
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "MinimumHidden.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBelowMinimumHidden() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "BelowMinimumHidden.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBelowMinimumVisible() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "BelowMinimumVisible.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAboveMaximumVisible() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "AboveMaximumVisible.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCardName() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "NoCardHand.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateCardVisVis() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "DuplicateCardVisVis.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateCardVisInv() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "DuplicateCardVisInv.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateCardInvInv() throws Exception
	{
		HandFactory.getDeck(Player.SELF, "resources" + File.separator
				+ "DuplicateCardInvInv.json");
	}

	@Test
	public void testCorrectPlayer() throws Exception
	{
		UndeployedCard[] cards = HandFactory.getDeck(Player.OPPONENT,
				"resources" + File.separator + "ThreeOpenExample.json");
		verifyCorrectPlayer(cards, Player.OPPONENT);
		verifyCorrectPlayer(threeOpenExample, Player.SELF);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonePlayer() throws Exception
	{
		HandFactory.getDeck(Player.NONE, "resources" + File.separator
				+ "ThreeOpenExample.json");
	}

	/**
	 * This method verifies that all of the cards in cards are held by the
	 * expected player.
	 * 
	 * @param cards
	 *            The cards to verify.
	 * @param expectedPlayer
	 *            The expected player of these cards.
	 */
	private static void verifyCorrectPlayer(UndeployedCard[] cards,
			Player expectedPlayer)
	{
		for (UndeployedCard card : cards)
		{
			if (card.isVisible())
			{
				assertEquals(expectedPlayer, card.deploy().holdingPlayer);
			} else
			{
				List<ProbCard> possibleCards = card.getPossibleCards();
				for (ProbCard pCard : possibleCards)
				{
					assertEquals(expectedPlayer, pCard.card.holdingPlayer);
				}
			}
		}
	}
}
