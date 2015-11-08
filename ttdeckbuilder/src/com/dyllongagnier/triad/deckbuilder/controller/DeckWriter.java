package com.dyllongagnier.triad.deckbuilder.controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.Player;

public class DeckWriter
{
	public static void writeDeckToDisk(String fileName, String[] cardNames) throws FileNotFoundException
	{
		// Do this to check deck validity.
		CardList.generateHand(Player.BLUE, cardNames);
		
		JsonObjectBuilder mainBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for(String cardName : cardNames)
		{
			arrBuilder.add(cardName);
		}
		mainBuilder.add("guaranteed", Json.createArrayBuilder().build());
		mainBuilder.add("maybe", arrBuilder.build());
		
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(fileName);
			writer.println(mainBuilder.build().toString());
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}
