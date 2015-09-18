package com.dyllongagnier.triad.core;

import java.util.EnumMap;
import java.util.SortedSet;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

public class AscensionBoardState extends BoardState {
	/**
	 * This initializes the AscensionBoardState. The protection level was left
	 * explicitly blank to ensure package protection since only the
	 * BoardState.Builder class and this class itself should be accessing this
	 * constructor.
	 * 
	 * @param playerHands
	 *            The cards of the player's hands.
	 * @param playedCards
	 *            The current field of the game.
	 */
	AscensionBoardState(EnumMap<Player, SortedSet<UndeployedCard>> playerHands,
			AscensionField playedCards) {
		super(playerHands, playedCards);
	}

}
