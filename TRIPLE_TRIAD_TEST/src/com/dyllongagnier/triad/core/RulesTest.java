package com.dyllongagnier.triad.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dyllongagnier.triad.core.functions.AscensionTransform;
import com.dyllongagnier.triad.core.functions.CardPlayFunction;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;
import com.dyllongagnier.triad.core.functions.FunctionAsserts;

public class RulesTest
{
	// Sudden death, no order, reverse, no fallen ace, is plus, not is same, is combo, normal ascension.
	public static final Rules rules1 = new Rules(true, false, true, false, true, false, true, Rules.AscensionRule.NORMAL);
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testConstructorBooleans()
	{
		assertTrue(rules1.isSuddenDeath);
		FunctionAsserts.assertEquals(AscensionTransform::ascension, rules1.ascensionFunc);
		FunctionAsserts.assertEquals(DeployedCardComparator::reverseCompare, rules1.cardComparator);
		FunctionAsserts.assertEquals(CardPlayFunction::plusCombo, rules1.playFunc);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testComboNoOther()
	{
		new Rules(true, true, true, true, false, false, true, Rules.AscensionRule.NONE);
	}
	
	@Test
	public void testSamePlus()
	{
		Rules test = new Rules(true, true, true, true, true, true, false, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(CardPlayFunction::samePlus, test.playFunc);
	}
	
	@Test
	public void testSameOnly()
	{
		Rules test = new Rules(true, true, true, true, true, false, false, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(CardPlayFunction::sameCapture, test.playFunc);
	}
	
	@Test
	public void testPlusOnly()
	{
		Rules test = new Rules(true, true, true, true, false, true, false, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(CardPlayFunction::plusCapture, test.playFunc);
	}
	
	@Test
	public void testSamePlusCombo()
	{
		Rules test = new Rules(true, true, true, true, true, true, true, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(CardPlayFunction::samePlusCombo, test.playFunc);
	}
	
	@Test
	public void testBasicCapture()
	{
		Rules test = new Rules(true, true, true, true, false, false, false, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(CardPlayFunction::samePlusCombo, test.playFunc);
	}
	
	@Test
	public void testNoTransform()
	{
		Rules test = new Rules(true, true, true, true, false, false, false, Rules.AscensionRule.NONE);
		FunctionAsserts.assertEquals(AscensionTransform::noAscension, test.ascensionFunc);
	}
	
	@Test
	public void testDescension()
	{
		Rules test = new Rules(true, true, true, true, false, false, false, Rules.AscensionRule.DESCENSION);
		FunctionAsserts.assertEquals(AscensionTransform::descension, test.ascensionFunc);
	}
	
	@Test
	public void testNoOrder()
	{
		Rules test = new Rules(true, false, true, true, false, false, false, Rules.AscensionRule.DESCENSION);
		throw new UnsupportedOperationException();
	}
}
