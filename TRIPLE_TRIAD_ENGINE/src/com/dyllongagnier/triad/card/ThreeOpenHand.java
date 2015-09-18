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
	 * This method creates an UndeployedCard array for the input deck and player
	 * assuming three open. See resources/TripleTriadCardList.json for an
	 * example of how to format the file.
	 * 
	 * @param player
	 *            The player of these cards.
	 * @param fileName
	 *            The file to read from.
	 * @return A new UndeployedCard[] to use as a hand.
	 * @throws FileNotFoundException
	 */
	public static UndeployedCard[] getDeck(Player player, String fileName)
			throws FileNotFoundException
	{
		if (player == Player.NONE)
			throw new IllegalArgumentException("Player must not be none.");
		
		JsonReader reader = Json.createReader(new FileInputStream(fileName));
		JsonObject ob;
		try
		{
			ob = reader.readObject();
		} finally
		{
			reader.close();
		}
		JsonArray guaranteed = ob.getJsonArray("guaranteed");
		if (guaranteed.size() != 3)
			throw new IllegalArgumentException(
					"Invalid file: guaranteed is not equal to 3.");
		
		UndeployedCard[] result = new UndeployedCard[5];
		int i = 0;
		HashSet<Card> inHand = new HashSet<>();
		
		for (JsonValue cardName : guaranteed)
		{
			result[i++] = getCardSafely(cardName, player, inHand);
		}
		assert i == 3;
		JsonArray randomized = ob.getJsonArray("maybe");
		if (randomized.size() < 3)
			throw new IllegalArgumentException(
					"Invalid file: randomized is less than three.");
		HashSet<Card> possibleCards = new HashSet<Card>();
		for (JsonValue cardName : randomized)
		{
			possibleCards.add(getCardSafely(cardName, player, inHand));
		}

		// Ensure that the random cards are a singleton.
		RandomCard rand = new RandomCard(possibleCards);
		result[i++] = rand;
		result[i++] = rand;
		assert i == 5;
		return result;
	}
	
	/**
	 * This method gets a card safely from the cardName and sets it to be held by the input player.
	 * This will throw an exception if the name of the card is invalid. It will also throw an exception if the card is already in the hand
	 * and will add this card to alreadyInHand.
	 * @param cardName The name of the card.
	 * @param player The holder of the card.
	 * @param alreadyInHand This set is used to determine if the input card is already in one's hand.
	 * @return A valid card.
	 */
	private static Card getCardSafely(JsonValue cardName, Player player, HashSet<Card> alreadyInHand)
	{
		assert player != Player.NONE;
		Card toAdd = CardList.getCard(((JsonString) cardName).getString());
		if (toAdd == null)
			throw new IllegalArgumentException(((JsonString)cardName).getString() + " is not a card.");
		else if (alreadyInHand.contains(toAdd))
			throw new IllegalArgumentException("Duplicate card:" + toAdd.name);
		else
		{
			alreadyInHand.add(toAdd);
			return toAdd.setHoldingPlayer(player);
		}
	}
}
