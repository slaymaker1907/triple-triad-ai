package com.dyllongagnier.triad.core;

import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.AscensionTransform;
import com.dyllongagnier.triad.core.functions.CardPlayFunction;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;
import com.dyllongagnier.triad.core.functions.MoveValidator;

/**
 * This is an immutable object for the purpose of conveying rules.
 */
public class Rules
{
	public final boolean isSuddenDeath;
	public final AscensionRule ascensionRule;
	public final MoveValidator moveValidator;
	public final CardPlayFunction playFunc;

	public static enum AscensionRule
	{
		NONE, NORMAL, DESCENSION
	}

	public final AscensionTransform ascensionFunc;
	public final DeployedCardComparator cardComparator;

	/**
	 * This method initializes an immutable Rules object.
	 * 
	 * @param isSuddenDeath
	 *            This indicates sudden death is in place.
	 * @param isOrder
	 *            This indicates that order is in place.
	 * @param isReverse
	 *            This indicates the reverse rule.
	 * @param isFallenAce
	 *            This indicates the fallen ace rule.
	 * @param ascensionRule
	 *            The way to handle ascension.
	 * @param selfCards
	 *            This is the card order to be used for isOrder for self.
	 * @param opponentCards
	 *            This is the card order to be used for isOrder for opponent.
	 */
	public Rules(boolean isSuddenDeath, boolean isOrder, boolean isReverse,
			boolean isFallenAce, boolean isPlus, boolean isSame,
			boolean isCombo, AscensionRule ascensionRule,
			UndeployedCard[] selfCards, UndeployedCard[] opponentCards)
	{
		this.isSuddenDeath = isSuddenDeath;
		this.moveValidator = getValidator(isOrder);
		this.ascensionFunc = getAscensionFunc(ascensionRule);
		this.cardComparator = getComparator(isReverse, isFallenAce);
		this.ascensionRule = ascensionRule;
		this.playFunc = getPlayFunction(isPlus, isSame, isCombo);
	}

	/**
	 * This method returns the comparator associated with this rule set.
	 * 
	 * @boolean isReverse Indicates that the Reverse rule is in place.
	 * @boolean isFallenAce Indicates that the Fallen Ace rule is in place.
	 * @return A comparator associated with the input rules.
	 */
	private static DeployedCardComparator getComparator(boolean isReverse,
			boolean isFallenAce)
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
	 * This method returns the correct AscensionTransform function for the given
	 * rules.
	 * 
	 * @return A method for ascension.
	 */
	private static AscensionTransform getAscensionFunc(AscensionRule rule)
	{
		switch (rule)
		{
			case NONE:
				return AscensionTransform::noAscension;
			case NORMAL:
				return AscensionTransform::ascension;
			case DESCENSION:
				return AscensionTransform::descension;
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * This method gets the relevant play function for the input options.
	 * 
	 * @param isPlus
	 *            Indicates the Plus rule.
	 * @param isSame
	 *            Indicates the Same rule.
	 * @param isCombo
	 *            Indicates the Combo rule. This can only be selected with
	 *            isPlus or isSame.
	 * @return The appropriate play function.
	 */
	private static CardPlayFunction getPlayFunction(boolean isPlus,
			boolean isSame, boolean isCombo)
	{
		if (!isPlus && !isSame && !isCombo)
			return CardPlayFunction::basicCapture;
		else if (isPlus && !isSame && !isCombo)
			return CardPlayFunction::plusCapture;
		else if (!isPlus && isSame && !isCombo)
			return CardPlayFunction::sameCapture;
		else if (isPlus && isSame && !isCombo)
			return CardPlayFunction::samePlus;
		else if (isPlus && !isSame && isCombo)
			return CardPlayFunction::plusCombo;
		else if (!isPlus && isSame && isCombo)
			return CardPlayFunction::sameCombo;
		else if (isPlus && isSame && isCombo)
			return CardPlayFunction::samePlusCombo;
		else
			throw new IllegalArgumentException(
					"Must select isPlus and/or isSame with isCombo.");
	}

	/**
	 * This method returns a validator for the given rule set.
	 * 
	 * @param isOrder
	 *            Whether to use the Order rule.
	 * @return A move validator for the relevant game.
	 */
	private static MoveValidator getValidator(boolean isOrder)
	{
		if (isOrder)
			return MoveValidator::normalValidator;
		else
			return MoveValidator::orderValidator;
	}
}
