package com.dyllongagnier.triad.net;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

public class PlayerDeck extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private Player player;
	private String[] deck;
	
	public PlayerDeck(Player player, String[] deck)
	{
		this.setPlayer(player);
		this.setDeck(deck);
	}
	
	public PlayerDeck(Player player, UndeployedCard[] cards)
	{
		this(player, getCardNames(cards));
	}
	
	private static String[] getCardNames(UndeployedCard[] cards)
	{
		String[] cardNames = new String[cards.length];
		int i = 0;
		for(UndeployedCard card : cards)
		{
			assert card.isVisible();
			cardNames[i++] = card.deploy().name;
		}
		return cardNames;
	}
	
	private void setPlayer(Player player)
	{
		this.player = player;
	}
	
	private void setDeck(String[] deck)
	{
		this.deck = deck;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public String[] getDeck()
	{
		return this.deck;
	}
	
	@Override
	public void processObjectServer(TriadServer server)
	{
		server.getController().setPlayerDeck(player, this.getDeck());
	}
}
