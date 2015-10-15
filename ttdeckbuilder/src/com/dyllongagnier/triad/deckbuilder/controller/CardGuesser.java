package com.dyllongagnier.triad.deckbuilder.controller;

import java.util.ArrayList;
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
		for(String name : sortedCards)
		{
			if (name.startsWith(partialName))
				return name;
		}
		
		return null;
	}
}
