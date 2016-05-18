package com.dyllongagnier.triad.core;

import java.util.EnumMap;
import java.util.Set;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.core.functions.AscensionTransform;
import com.dyllongagnier.triad.core.functions.CardPlayFunction;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;

/**
 * This class is exactly like Field except for the fact that it uses ascension.
 */
public class AscensionField extends Field
{
	private final EnumMap<Card.Type, Integer> typeMap;
	private final AscensionTransform ascensionTransform;
	private final DeployedCardComparator originalComp;

	/**
	 * This method initializes an empty field.
	 * 
	 * @param cardComparator
	 *            The comparator to use for battles.
	 * @param ascensionTransform
	 *            The ascension function to use.
	 */
	public AscensionField(DeployedCardComparator cardComparator,
			AscensionTransform ascensionTransform, CardPlayFunction playFunc)
	{
		super(cardComparator, playFunc);
		this.originalComp = cardComparator;
		this.typeMap = initTypeMap();
		this.cardComparator = (toPlay, played) -> cardComparator.apply(this.buffCard(toPlay), this.buffCard(played));
		this.ascensionTransform = ascensionTransform;
	}
	
	public DeployedCard buffCard(DeployedCard card)
	{
		switch(card.card.cardType)
		{
			case NONE:
				return card;
			default:
				return this.ascensionTransform.apply(card, this.typeMap.get(card.card.cardType));
		}
	}
	
	/**
	 * This method returns the number of cards played with the specified type.
	 * @param type The type to lookup.
	 * @return The number of cards with type type.
	 */
	public int getAscensionNumber(Card.Type type)
	{
		if (type == Card.Type.NONE)
			return 0;
		else
			return this.typeMap.get(type);
	}

	/**
	 * This method initializes a type map with each card type (except for none)
	 * pointing to an empty array.
	 * 
	 * @return A initialized type map for use in the constructor.
	 */
	private static EnumMap<Card.Type, Integer> initTypeMap()
	{
		EnumMap<Card.Type, Integer> result = new EnumMap<>(
				Card.Type.class);
		result.put(Card.Type.BEASTMAN, 0);
		result.put(Card.Type.GARLEAN, 0);
		result.put(Card.Type.PRIMAL, 0);
		result.put(Card.Type.SCION, 0);
		return result;
	}

	/**
	 * This method initializes a field using the input data.
	 * 
	 * @param playedCards
	 *            The cards already in play.
	 * @param cardComparator
	 *            The comparator to use for card battles.
	 * @param typeMap
	 *            The type map to use for finding cards of a specified type.
	 * @param ascensionTransform
	 *            The ascension function to use.
	 */
	protected AscensionField(DeployedCard[][] playedCards,
			DeployedCardComparator cardComparator,
			EnumMap<Card.Type, Integer> typeMap,
			AscensionTransform ascensionTransform, CardPlayFunction playFunc)
	{
		super(playedCards, cardComparator, playFunc);
		this.cardComparator = (toPlay, played) -> cardComparator.apply(this.buffCard(toPlay), this.buffCard(played));
		this.originalComp = cardComparator;
		this.typeMap = typeMap;
		this.ascensionTransform = ascensionTransform;
	}

	@Override
	public Field playCard(DeployedCard cardToPlay)
	{
		assert cardToPlay != null;
		int row = cardToPlay.row;
		int col = cardToPlay.col;
		assert !this.isCardInPos(row, col);

		Set<DeployedCard> takeOver = this.playFunc.updateField(this,
				cardToPlay, this.cardComparator);
		DeployedCard[][] newPlayedCards = this.copyPlayedCards();
		newPlayedCards[row][col] = cardToPlay;
		for (DeployedCard card : takeOver)
		{
			DeployedCard newCard = card
					.setPlayer(cardToPlay.card.holdingPlayer);
			newPlayedCards[newCard.row][newCard.col] = newCard;
		}
		
		EnumMap<Card.Type, Integer> newTypeMap = this.typeMap;
		if (cardToPlay.card.cardType != Card.Type.NONE)
		{
			newTypeMap = new EnumMap<>(this.typeMap);
			newTypeMap.compute(cardToPlay.card.cardType, (type, old) -> old + 1);
		}
		return new AscensionField(newPlayedCards, this.originalComp, newTypeMap, this.ascensionTransform, this.playFunc);
	}
}
