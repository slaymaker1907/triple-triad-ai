package com.dyllongagnier.triad.core.functions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.core.Field;

@FunctionalInterface
public interface CardPlayFunction 
{
	static class SumPairCollection
	{
		private final Map<Integer, Set<DeployedCard>> mapOfSums;
		
		public SumPairCollection()
		{
			this.mapOfSums = new HashMap<>();
		}
		
		public boolean addPair(DeployedCard playedCard, DeployedCard otherCard)
		{
			assert playedCard.cardAdjacent(otherCard);
			if (otherCard == null)
				return false;
			int sum = CardPlayFunction.computeSum(playedCard, otherCard);
			if (!this.mapOfSums.containsKey(sum))
				this.mapOfSums.put(sum, new HashSet<DeployedCard>());
			this.mapOfSums.get(sum).add(otherCard);
			return true;
		}
		
		public Set<DeployedCard> cardsToCapture()
		{
			Set<DeployedCard> result = new HashSet<DeployedCard>();
			for (Entry<Integer, Set<DeployedCard>> pair : this.mapOfSums.entrySet())
			{
				if (pair.getValue().size() >= 2)
					result.addAll(this.mapOfSums.get(pair.getKey()));
			}
			return result;
		}
	}
	
	public Set<DeployedCard> updateField(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator);
	
	public static Set<DeployedCard> basicCapture(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = new HashSet<>(4);
		int row = toPlay.row;
		int col = toPlay.col;
		tryCapture(toPlay, field.getCard(row - 1, col), cardComparator, result);
		tryCapture(toPlay, field.getCard(row + 1, col), cardComparator, result);
		tryCapture(toPlay, field.getCard(row, col - 1), cardComparator, result);
		tryCapture(toPlay, field.getCard(row, col - 1), cardComparator, result);
		
		return result;
	}
	
	public static Set<DeployedCard> sameCapture(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.basicCapture(field, toPlay, cardComparator);
		Set<DeployedCard> sameCards = CardPlayFunction.basicCapture(field, toPlay, DeployedCardComparator::equalCompare);
		if (sameCards.size() >= 2)
			result.addAll(sameCards);
		return result;
	}
	
	static int computeSum(DeployedCard toPlay, DeployedCard otherCard)
	{
		assert toPlay.cardAdjacent(otherCard);
		if (otherCard == null)
		{
			return -1;
		}
		DeployedCard.Direction dir = toPlay.getDirectionOfOther(otherCard);
		switch(dir)
		{
		case NORTH:
			return toPlay.card.north + otherCard.card.south;
		case EAST:
			return toPlay.card.east + otherCard.card.west;
		case SOUTH:
			return toPlay.card.south + otherCard.card.north;
		case WEST:
			return toPlay.card.west + toPlay.card.east;
		default:
			throw new RuntimeException("Unsupported enum value.");
		}
	}
	
	public static Set<DeployedCard> plusCapture(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.basicCapture(field, toPlay, cardComparator);
		int row = toPlay.row;
		int col = toPlay.col;
		
		// This could maybe be a little better.
		SumPairCollection sums = new SumPairCollection();
		sums.addPair(toPlay, field.getCard(row + 1, col));
		sums.addPair(toPlay, field.getCard(row - 1, col));
		sums.addPair(toPlay, field.getCard(row, col + 1));
		sums.addPair(toPlay, field.getCard(row, col - 1));
		result.addAll(sums.cardsToCapture());
		
		return result;
	}
	
	public static Set<DeployedCard> plusCombo(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> capturedThroughBattle = CardPlayFunction.basicCapture(field, toPlay, cardComparator);
		int row = toPlay.row;
		int col = toPlay.col;
		
		// This could maybe be a little better.
		SumPairCollection sums = new SumPairCollection();
		sums.addPair(toPlay, field.getCard(row + 1, col));
		sums.addPair(toPlay, field.getCard(row - 1, col));
		sums.addPair(toPlay, field.getCard(row, col + 1));
		sums.addPair(toPlay, field.getCard(row, col - 1));
		Set<DeployedCard> capturedThroughRule = sums.cardsToCapture();
		for(DeployedCard card : capturedThroughRule)
		{
			capturedThroughBattle.addAll(CardPlayFunction.recursiveNormalCapture(field, card, cardComparator));
		}
		capturedThroughBattle.addAll(capturedThroughRule);
		return capturedThroughBattle;
	}
	
	public static Set<DeployedCard> sameCombo(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> capturedThroughBattle = CardPlayFunction.basicCapture(field, toPlay, cardComparator);
		
		Set<DeployedCard> sameCards = CardPlayFunction.basicCapture(field, toPlay, DeployedCardComparator::equalCompare);
		if (sameCards.size() >= 2)
		{
			for(DeployedCard card : sameCards)
			{
				capturedThroughBattle.addAll(CardPlayFunction.recursiveNormalCapture(field, card, cardComparator));
			}
			capturedThroughBattle.addAll(sameCards);
		}
		
		return sameCards;
	}
	
	public static Set<DeployedCard> samePlusCombo(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		// Take the union of the two sets.
		Set<DeployedCard> result = CardPlayFunction.sameCombo(field, toPlay, cardComparator);
		result.addAll(CardPlayFunction.plusCombo(field, toPlay, cardComparator));
		
		return result;
	}
	
	public static Set<DeployedCard> samePlus(Field field, DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.plusCapture(field, toPlay, cardComparator);
		result.addAll(CardPlayFunction.sameCapture(field, toPlay, cardComparator));
		return result;
	}
	
	static Set<DeployedCard> recursiveNormalCapture(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = new HashSet<>();
		Set<DeployedCard> captured = CardPlayFunction.basicCapture(field, toPlay, cardComparator);
		for(DeployedCard card : captured)
		{
			result.addAll(CardPlayFunction.basicCapture(field, card, cardComparator));
		}
		result.addAll(captured);
		return result;
	}
		
	static void tryCapture(DeployedCard justPlayed, DeployedCard toCapture, DeployedCardComparator
			cardComparator, Set<DeployedCard> result)
	{
		if (toCapture == null)
			return;
		assert justPlayed.cardAdjacent(toCapture);
		if (cardComparator.apply(justPlayed, toCapture))
			result.add(toCapture);
	}
}
