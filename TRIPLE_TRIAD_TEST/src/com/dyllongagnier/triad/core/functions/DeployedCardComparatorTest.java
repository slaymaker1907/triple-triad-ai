package com.dyllongagnier.triad.core.functions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.DeployedCard;

public class DeployedCardComparatorTest
{
	public static final Card dodo = CardList.getCard("Dodo");
	public static final Card gaelicat = CardList.getCard("Gaelicat");
	public static final Card spriggan = CardList.getCard("Spriggan");
	public static final Card coeurl = CardList.getCard("Coeurl");
	public static final Card nidhogg = CardList.getCard("Nidhogg");
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testCaptureNorthRegular()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 0);
		DeployedCard played = new DeployedCard(dodo, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testNoCaptureNorthRegular()
	{
		DeployedCard inPlace = new DeployedCard(dodo, 0, 0);
		DeployedCard played = new DeployedCard(spriggan, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testCaptureEastRegular()
	{
		DeployedCard inPlace = new DeployedCard(dodo, 0, 1);
		DeployedCard played = new DeployedCard(coeurl, 0, 0);
		assertEquals(DeployedCard.Direction.EAST,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testNoCaptureEastRegular()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 1);
		DeployedCard played = new DeployedCard(dodo, 0, 0);
		assertEquals(DeployedCard.Direction.EAST,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testCaptureSouthRegular()
	{
		DeployedCard inPlace = new DeployedCard(coeurl, 1, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testNoCaptureSouthRegular()
	{
		DeployedCard inPlace = new DeployedCard(dodo, 1, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testCaptureWestRegular()
	{
		DeployedCard inPlace = new DeployedCard(dodo, 0, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 1);
		assertEquals(DeployedCard.Direction.WEST,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testNoCaptureWestRegular()
	{
		DeployedCard inPlace = new DeployedCard(coeurl, 0, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 1);
		assertEquals(DeployedCard.Direction.WEST,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testNoCaptureEquality()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 1, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.regularCompare(played, inPlace));
	}
	
	@Test
	public void testReverseCompareEquality()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 1, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.reverseCompare(played, inPlace));
	}
	
	@Test
	public void testReverseCompare()
	{
		DeployedCard inPlace = new DeployedCard(coeurl, 0, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 1);
		assertEquals(DeployedCard.Direction.WEST,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.reverseCompare(played, inPlace));
	}
	
	@Test
	public void testFallenAceCompare()
	{
		DeployedCard inPlace = new DeployedCard(nidhogg, 1, 0); // Ace is north stat.
		DeployedCard played = new DeployedCard(gaelicat, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertEquals(10, inPlace.card.north);
		assertEquals(1, played.card.south);
		assertTrue(DeployedCardComparator.fallenAceCompare(played, inPlace));
	}
	
	@Test
	public void testFallenAceRegularCompare()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 0);
		DeployedCard played = new DeployedCard(dodo, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.fallenAceCompare(played, inPlace));
	}
	
	@Test
	public void testFallenAceNoCapture()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 0); // Ace is north stat.
		DeployedCard played = new DeployedCard(nidhogg, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertEquals(10, played.card.north);
		assertEquals(1, inPlace.card.south);
		assertFalse(DeployedCardComparator.fallenAceCompare(played, inPlace));
	}
	
	@Test
	public void testFallenAceReverseCompare()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 0);
		DeployedCard played = new DeployedCard(nidhogg, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertEquals(10, played.card.north);
		assertEquals(1, inPlace.card.south);
		assertTrue(DeployedCardComparator.fallenAceReverseCompare(played, inPlace));
	}
	
	@Test
	public void testFallenAceReverseNoCapture()
	{
		DeployedCard inPlace = new DeployedCard(nidhogg, 1, 0); // Ace is north stat.
		DeployedCard played = new DeployedCard(gaelicat, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertEquals(10, inPlace.card.north);
		assertEquals(1, played.card.south);
		assertFalse(DeployedCardComparator.fallenAceReverseCompare(played, inPlace));
	}
	
	@Test
	public void testCompareEquality()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 1, 0);
		DeployedCard played = new DeployedCard(spriggan, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertTrue(DeployedCardComparator.equalCompare(played, inPlace));
	}
	
	@Test
	public void testCompareEqualityGt()
	{
		DeployedCard inPlace = new DeployedCard(nidhogg, 1, 0);
		DeployedCard played = new DeployedCard(gaelicat, 0, 0);
		assertEquals(DeployedCard.Direction.SOUTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.equalCompare(played, inPlace));
	}
	
	@Test
	public void testCompareEqualityLt()
	{
		DeployedCard inPlace = new DeployedCard(gaelicat, 0, 0);
		DeployedCard played = new DeployedCard(nidhogg, 1, 0);
		assertEquals(DeployedCard.Direction.NORTH,played.getDirectionOfOther(inPlace));
		assertFalse(DeployedCardComparator.equalCompare(played, inPlace));
	}
}
