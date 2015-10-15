package com.dyllongagnier.triad.card;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

public class HandFactory
{
	private HandFactory()
	{
	}

	/**
	 * This method creates an UndeployedCard array for the input deck and
	 * player. See resources/TripleTriadCardList.json for an example of how to
	 * format the file.
	 * 
	 * @param player
	 *            The player of these cards.
	 * @param fileName
	 *            The file to read from.
	 * @param maybeCardGenerator
	 * 			   A function that converts a collection of cards into a singleton representing one card.
	 * @return A new UndeployedCard[] to use as a hand.
	 * @throws FileNotFoundException
	 */
	public static UndeployedCard[] getDeck(Player player, String fileName, Function<Collection<Card>, UndeployedCard>
		maybeCardGenerator)
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
		if (guaranteed.size() > 5)
			throw new IllegalArgumentException(
					"Invalid file: can not have more than 5 guaranteed cards");

		ArrayList<UndeployedCard> result = new ArrayList<>();
		int handSize = 0;
		for (JsonValue cardName : guaranteed)
		{
			result.add(getCardSafely(cardName, player, handSize++));
		}

		JsonArray randomized = ob.getJsonArray("maybe");
		if (randomized.size() +  guaranteed.size()< 5)
			throw new IllegalArgumentException(
					"Invalid file: randomized and guaranteed cards must sum to at least 5.");

		for (JsonValue cardName : randomized)
		{
			result.add(getCardSafely(cardName, player, handSize++));
		}
		
		return result.toArray(new UndeployedCard[result.size()]);
	}
	
	public static UndeployedCard[] getDeck(Player player, String fileName) throws FileNotFoundException
	{
		return HandFactory.getDeck(player, fileName, RandomCard::new);
	}

	/**
	 * This method gets a card safely from the cardName and sets it to be held
	 * by the input player. This will throw an exception if the name of the card
	 * is invalid. It will also throw an exception if the card is already in the
	 * hand and will add this card to alreadyInHand.
	 * 
	 * @param cardName
	 *            The name of the card.
	 * @param player
	 *            The holder of the card.
	 * @param alreadyInHand
	 *            This set is used to determine if the input card is already in
	 *            one's hand.
	 * @return A valid card.
	 */
	private static OrderedCard getCardSafely(JsonValue cardName, Player player, int handSize)
	{
		assert player != Player.NONE;
		Card toAdd = CardList.getCard(((JsonString) cardName).getString());
		if (toAdd == null)
			throw new IllegalArgumentException(
					((JsonString) cardName).getString() + " is not a card.");
		else
		{
			return new OrderedCard(toAdd.setHoldingPlayer(player), handSize);
		}
	}
}
