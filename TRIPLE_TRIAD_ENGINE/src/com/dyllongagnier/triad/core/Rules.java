package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.AscensionTransform;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;

/**
 * This is an immutable object for the purpose of conveying rules.
 */
public class Rules 
{
	public final boolean isSuddenDeath;
	public final boolean isOrder;
	public final boolean isChaos;
	public final AscensionRule ascensionRule;
	
	public static enum AscensionRule
	{
		NONE, NORMAL, DESCENSION
	}
	
	public final AscensionTransform ascensionFunc;
	public final DeployedCardComparator cardComparator;
		
	/**
	 * This method initializes an immutable Rules object.
	 * @param isSuddenDeath This indicates sudden death is in place.
	 * @param isOrder This indicates that order is in place.
	 * @param isChaos This indicates the chaos rule.
	 * @param isReverse This indicates the reverse rule.
	 * @param isFallenAce This indicates the fallen ace rule.
	 * @param ascensionRule The way to handle ascension.
	 * @param selfCards This is the card order to be used for isOrder for self.
	 * @param opponentCards This is the card order to be used for isOrder for opponent.
	 */
	public Rules(boolean isSuddenDeath, boolean isOrder, boolean isChaos, boolean isReverse, boolean isFallenAce,
			AscensionRule ascensionRule, UndeployedCard[] selfCards, UndeployedCard[] opponentCards)
	{
		this.isSuddenDeath = isSuddenDeath;
		this.isOrder = isOrder;
		this.isChaos = isChaos;
		this.ascensionFunc = getAscensionFunc(ascensionRule);
		this.cardComparator = getComparator(isReverse, isFallenAce);
		this.ascensionRule = ascensionRule;
	}
	
	/**
	 * This method returns the comparator associated with this rule set.
	 * @boolean isReverse Indicates that the Reverse rule is in place.
	 * @boolean isFallenAce Indicates that the Fallen Ace rule is in place.
	 * @return A comparator associated with the input rules.
	 */
	private static DeployedCardComparator getComparator(boolean isReverse, boolean isFallenAce)
	{
		if (isReverse && isFallenAce)
			return DeployedCardComparator::fallenAceReverseCompare;
		else if (isReverse)
			return DeployedCardComparator::reverseCompare;
		else if (isFallenAce)
			return DeployedCardComparator::fallenAceCompare;
		else
			return DeployedCardComparator::regularCompare;
	}
	
	/**
	 * This method returns the correct AscensionTransform function for the given rules.
	 * @return A method for ascension.
	 */
	private static AscensionTransform getAscensionFunc(AscensionRule rule)
	{
		switch(rule)
		{
		case  NONE:
			return AscensionTransform::noAscension;
		case NORMAL:
			return AscensionTransform::ascension;
		case DESCENSION:
			return AscensionTransform::descension;
		default:
			throw new IllegalArgumentException();
		}
	}
}
