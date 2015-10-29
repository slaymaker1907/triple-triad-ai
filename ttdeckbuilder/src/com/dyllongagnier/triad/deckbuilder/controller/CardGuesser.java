package com.dyllongagnier.triad.deckbuilder.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.CardList;

public class CardGuesser
{
	private static final ArrayList<String> sortedCards;
	
	static
	{
		Function<Card, String> nameFunc = (card) -> card.name;
		sortedCards = new ArrayList<String>(CardList.getAllCards().stream().map(nameFunc).collect(Collectors.toList()));
		sortedCards.sort(String::compareTo);
	}
	
	public static String getClosestMatch(String partialName)
	{
		int point = Collections.binarySearch(sortedCards, partialName);
		int negPoint = -1 * point - 1;
		if (point >= 0)
			return sortedCards.get(point);
		else if (negPoint < sortedCards.size())
		{
			String fullName = sortedCards.get(negPoint);
			if (fullName.startsWith(partialName))
				return fullName;
			else
				return null;
		}
		else
			return null;
	}
	
	public static boolean isValidCard(String name)
	{
		return CardList.getCard(name) != null;
	}
}
