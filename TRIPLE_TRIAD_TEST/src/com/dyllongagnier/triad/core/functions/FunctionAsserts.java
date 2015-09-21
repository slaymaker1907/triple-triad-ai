package com.dyllongagnier.triad.core.functions;

import org.junit.Assert;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.DeployedCard;

public class FunctionAsserts
{
	public static final Card dodo = CardList.getCard("Dodo");
	public static final Card gaelicat = CardList.getCard("Gaelicat");
	public static final Card spriggan = CardList.getCard("Spriggan");
	public static final Card nidhogg = CardList.getCard("Nidhogg");	
	public static final Card kobold = CardList.getCard("Kobold");
	
	/**
	 * This method runs a number of tests over the input functions to determine if they are equal,
	 * throwing an AssertionError otherwise.
	 * @param expected The expected function.
	 * @param actual The actual function.
	 */
	public static void assertEquals(DeployedCardComparator expected, DeployedCardComparator actual)
	{
		// These two distinguish between reverse and non-reverse comparators.
		DeployedCard middleDodo = new DeployedCard(dodo, 1, 1);
		DeployedCard upMidGael = new DeployedCard(gaelicat, 0, 1);
		
		// This and middleDodo will discover if the function is equality.
		DeployedCard upMidSpriggan = new DeployedCard(spriggan, 0, 1);
		
		// This and upMidGael will determine if Fallen Ace is in effect.
		DeployedCard middleNid = new DeployedCard(nidhogg, 1, 1);
		
		// Check just a regular, non-ace compare.
		Assert.assertEquals(expected.apply(middleDodo, upMidGael), actual.apply(middleDodo, upMidGael));
		
		// Check to see if it neither captures if one is equality.
		Assert.assertEquals(expected.apply(middleDodo, upMidSpriggan), actual.apply(middleDodo, upMidSpriggan));
		Assert.assertEquals(expected.apply(upMidSpriggan, middleDodo), actual.apply(upMidSpriggan, middleDodo));
		
		// Check an ace comparison with a 1.
		Assert.assertEquals(expected.apply(upMidGael, middleNid), actual.apply(upMidGael, middleNid));
	}
	
	/**
	 * This method runs a number of tests over the input functions to determine if they are equal,
	 * throwing an AssertionError otherwise.
	 * @param expected The expected function.
	 * @param actual The actual function.
	 */
	public static void assertEquals(AscensionTransform expected, AscensionTransform actual)
	{
		DeployedCard card = new DeployedCard(kobold, 0, 0);
		Assert.assertEquals(expected.apply(card, 100), actual.apply(card, 100));
	}
	
	/**
	 * This method runs a number of tests over the input functions to determine if they are equal,
	 * throwing an AssertionError otherwise.
	 * @param expected The expected function.
	 * @param actual The actual function.
	 */
	public static void assertEquals(CardPlayFunction expected, CardPlayFunction actual)
	{
		throw new UnsupportedOperationException("This method is not yet implemented.");
		
		// Determine if the same rule is in effect.
		// Determine if the plus rule is in effect.
		// Determine if the Combo rule is in effect with either same or plus.
	}
}
