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
}
