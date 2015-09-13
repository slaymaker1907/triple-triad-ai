package com.dyllongagnier.triad.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.Player;

public class BoardState
{
	private final EnumMap<Player, Set<Card>> playerHands = new EnumMap<>(Player.class);
	private final Card[][] playedCards;
	
	protected BoardState(EnumMap<Player, Set<Card>> playerHands, Card[][] playedCards)
	{
		Set<Card> opponentCards = Collections.unmodifiableSet(playerHands.get(Player.OPPONENT));
		Set<Card> selfCards = Collections.unmodifiableSet(playerHands.get(Player.SELF));
		this.playerHands.put(Player.OPPONENT, opponentCards);
		this.playerHands.put(Player.SELF, selfCards);
		this.playedCards = playedCards;
	}
	
	public Set<Card> getHand(Player player)
	{
		if (player == Player.NONE)
			throw new IllegalArgumentException();
		if (player == null)
			throw new NullPointerException();
		return this.playerHands.get(player);
	}
	
	public Card getPlayedCard(int row, int col)
	{
		// The played card array can either be null if no card or non-null if not empty.
		return playedCards[row][col];
	}
	
	public static class Builder
	{
		private final EnumMap<Player, Set<Card>> playerHands;
		private final Card[][] playedCards = new Card[9][9];
		
		public Builder()
		{
			this.playerHands = new EnumMap<>(Player.class);
			this.playerHands.put(Player.SELF, new HashSet<>());
			this.playerHands.put(Player.OPPONENT, new HashSet<>());
		}
		
		public Builder placeCardSafely(Card card, int row, int col)
		{
			if (playedCards[row][col] != null)
				throw new IllegalArgumentException("Row:" + row + " Col:" + col);
			playedCards[row][col] = card;
			return this;
		}
		
		public Builder playCardFromHand(Player player, Card card, int row, int col)
		{
			return this.removeFromHand(player, card).placeCardSafely(card, row, col);
		}
		
		public Builder setHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			playerCards.clear();
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}
		
		public Builder addToHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			playerCards.addAll(Arrays.asList(cards));
			return this;
		}
		
		public Builder removeFromHand(Player player, Card ... cards)
		{
			if (player == null || cards == null)
				throw new NullPointerException();
			if (player == Player.NONE)
				throw new IllegalArgumentException();
			
			Set<Card> playerCards = playerHands.get(player);
			playerCards.removeAll(Arrays.asList(cards));
			return this;
		}
		
		public BoardState build()
		{
			return new BoardState(this.playerHands, this.playedCards);
		}
	}
}
