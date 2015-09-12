package com.dyllongagnier.triad.card;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class CardList
{
	public static final Map<String, Card> allCards = CardList.getCardsFromJson("resources" + File.separator + "TripleTriadCardList.json");	
	
	protected CardList()
	{
	}
	
	public static Card getCard(String name)
	{
		return CardList.allCards.get(name);
	}
	
	public static Collection<Card> getAllCards()
	{
		return Collections.unmodifiableCollection(allCards.values());
	}
	
	public static Map<String, Card> getCardsFromJson(String fileName)
	{
		HashMap<String, Card> result = new HashMap<>();
		try
		{
			JsonReader reader = Json.createReader(new FileInputStream(fileName));
			JsonObject rankLists = reader.readObject();
			reader.close();
			for(int rank = 1; rank <= 5; rank++)
			{
				result.putAll(CardList.getCardsFromRankList(rankLists.getJsonArray("rank_" + rank ), rank));
			}
			return Collections.unmodifiableMap(result);
		}
		catch (Exception e)
		{
			System.err.println("Could not initialize card database.");
			e.printStackTrace();
			return null;
		}
	}
	
	private static Map<String, Card> getCardsFromRankList(JsonArray cardList, int rank)
	{
		HashMap<String, Card> result = new HashMap<>();
		for(JsonValue val : cardList)
		{
			JsonObject card = (JsonObject)val;
			String name = card.getString("name");
			Card.Type cardType = Card.Type.valueOf(card.getString("type"));
			result.put(name, new Card(card.getInt("north"), card.getInt("east"), card.getInt("south"), card.getInt("west"), name, cardType, rank));
		}
		return Collections.unmodifiableMap(result);
	}
}
