package com.dyllongagnier.triad.core;

import java.util.Arrays;
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
	private final EnumMap<Card.Type, DeployedCard[]> typeMap;
	private final AscensionTransform ascensionTransform;
	
	/**
	 * This method initializes an empty field.
	 * @param cardComparator The comparator to use for battles.
	 * @param ascensionTransform The ascension function to use.
	 */
	public AscensionField(DeployedCardComparator cardComparator, AscensionTransform ascensionTransform,
			CardPlayFunction playFunc)
	{
		super(cardComparator, playFunc);
		this.typeMap = initTypeMap();
		this.ascensionTransform = ascensionTransform;
	}
	
	/**
	 * This method initializes a type map with each card type (except for none) pointing to an empty array.
	 * @return A initialized type map for use in the constructor.
	 */
	private static EnumMap<Card.Type, DeployedCard[]>  initTypeMap()
	{
		EnumMap<Card.Type, DeployedCard[]> result = new EnumMap<>(Card.Type.class);
		result.put(Card.Type.BEASTMAN, new DeployedCard[0]);
		result.put(Card.Type.GARLEAN, new DeployedCard[0]);
		result.put(Card.Type.PRIMAL, new DeployedCard[0]);
		result.put(Card.Type.SCION, new DeployedCard[0]);
		return result;
	}
	
	/**
	 * This method initializes a field using the input data.
	 * @param playedCards The cards already in play.
	 * @param cardComparator The comparator to use for card battles.
	 * @param typeMap The type map to use for finding cards of a specified type.
	 * @param ascensionTransform The ascension function to use.
	 */
	public AscensionField(DeployedCard[][] playedCards, DeployedCardComparator cardComparator,
			EnumMap<Card.Type, DeployedCard[]> typeMap, AscensionTransform ascensionTransform, 
			CardPlayFunction playFunc)
	{
		super(playedCards, cardComparator, playFunc);
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
		
		EnumMap<Card.Type, DeployedCard[]> newTypeMap = this.typeMap;
		DeployedCard[][] newPlayedCards;
		if (cardToPlay.card.cardType != Card.Type.NONE)
		{
			// Make sure not to mutate the old map.
			newPlayedCards = this.increaseTypeStats(cardToPlay.card.cardType);
			newTypeMap = new EnumMap<Card.Type, DeployedCard[]>(this.typeMap);
			cardToPlay = this.ascensionTransform.apply(cardToPlay, addCardToMap(newTypeMap, cardToPlay) - 1);
		}
		else
			newPlayedCards = this.playedCards.clone();
		
		Set<DeployedCard> takeOver = this.playFunc.updateField(this, cardToPlay, this.cardComparator);
		newPlayedCards[row][col] = cardToPlay;
		for(DeployedCard card : takeOver)
		{
			DeployedCard newCard = card.setPlayer(cardToPlay.card.holdingPlayer);
			newPlayedCards[newCard.row][newCard.col] = newCard;
		}
		
		newPlayedCards[row][col] = cardToPlay;
		return new AscensionField(newPlayedCards, this.cardComparator, newTypeMap, this.ascensionTransform,
				this.playFunc);
	}
	
	/**
	 * This method adds the input card to the type map and returns the total size of the array
	 * in the type map after adding the card.
	 * @param typeMap The type map to mutate.
	 * @param toAdd The card to add.
	 * @return The total size of the array for the type of toAdd after toAdd is added.
	 */
	private static int addCardToMap(EnumMap<Card.Type, DeployedCard[]> typeMap, DeployedCard toAdd)
	{
		assert toAdd.card.cardType != Card.Type.NONE;
		assert typeMap != null;
		
		DeployedCard[] oldArr = typeMap.get(toAdd.card.cardType);
		DeployedCard[] newArr = Arrays.copyOf(oldArr, oldArr.length + 1);
		assert newArr.length == oldArr.length + 1;
		newArr[oldArr.length] = toAdd;
		typeMap.put(toAdd.card.cardType, newArr);
		assert newArr.length > 0;
		return newArr.length;
	}
	
	/**
	 * This method increases the input type's deployed cards by 1.
	 * @param typeToIncrease The type to increase the stats of.
	 * @return A new DeployedCard[][] with the reflected stat changes.
	 */
	private DeployedCard[][] increaseTypeStats(Card.Type typeToIncrease)
	{
		DeployedCard[][] result = this.playedCards.clone();
		for(DeployedCard card : this.typeMap.get(typeToIncrease))
		{
			result[card.row][card.col] = this.ascensionTransform.apply(card, 1);
		}
		return result;
	}
}
