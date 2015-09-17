package com.dyllongagnier.triad.card;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

public class ThreeOpenHand 
{
	private ThreeOpenHand()
	{
	}
	
	/**
	 * This method creates an UndeployedCard array for the input deck and player assuming three open. See
	 * resources/TripleTriadCardList.json for an example of how to format the file.
	 * @param player The player of these cards.
	 * @param fileName The file to read from.
	 * @return A new UndeployedCard[] to use as a hand.
	 * @throws FileNotFoundException
	 */
	public UndeployedCard[] getDeck(Player player, String fileName) throws FileNotFoundException
	{
		JsonReader reader = Json.createReader(new FileInputStream(fileName));
		JsonObject ob;
		try
		{
			ob = reader.readObject();
		}
		finally
		{
			reader.close();
		}
		JsonArray guaranteed = ob.getJsonArray("guaranteed");
		if (guaranteed.size() != 3)
			throw new IllegalArgumentException("Invalid file: guaranteed is not equal to 3.");
		UndeployedCard[] result = new UndeployedCard[5];
		int i = 0;
		for(JsonValue cardName : guaranteed)
		{
			result[i++] = CardList.getCard(((JsonString)cardName).getString()).setHoldingPlayer(player);
		}
		assert i == 3;
		JsonArray randomized = ob.getJsonArray("maybe");
		HashSet<Card> possibleCards = new HashSet<Card>();
		for(JsonValue cardName : randomized)
		{
			possibleCards.add(CardList.getCard(((JsonString)cardName).getString()).setHoldingPlayer(player));
		}
		result[i++] = new RandomCard(possibleCards);
		result[i++] = new RandomCard(possibleCards);
		assert i == 5;
		return result;
	}
}
