package com.dyllongagnier.triad.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.Function;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.CardPlayFunction;
import com.dyllongagnier.triad.core.functions.DeployedCardComparator;

/**
 * This class represents a field of cards. This object is immutable.
 */
public class Field
{
	protected final DeployedCard[][] playedCards;
	protected DeployedCardComparator cardComparator;
	protected final CardPlayFunction playFunc;

	/**
	 * Creates an empty field that will use the input cardComparator.
	 * 
	 * @param cardComparator
	 *            The comparator to use for determining victories.
	 */
	public Field(DeployedCardComparator cardComparator,
			CardPlayFunction playFunc)
	{
		this.playedCards = new DeployedCard[3][3];
		this.cardComparator = cardComparator;
		this.playFunc = playFunc;
	}

	/**
	 * This method creates a new field with the input cards and the input
	 * comparator.
	 * 
	 * @param playedCards
	 *            The cards of the field.
	 * @param cardComparator
	 *            The comparator to use in comparing cards.
	 */
	protected Field(DeployedCard[][] playedCards,
			DeployedCardComparator cardComparator, CardPlayFunction playFunc)
	{
		this.playedCards = playedCards;
		this.cardComparator = cardComparator;
		this.playFunc = playFunc;
	}

	/**
	 * This method returns the card at row/col. This will be null if there is no
	 * card played there.
	 * 
	 * @param row
	 *            The row of the card to retrieve.
	 * @param col
	 *            The column of the card to retrieve.
	 * @return The card at row/col.
	 */
	public DeployedCard getCard(int row, int col)
	{
		if (!isInBounds(row, col))
			return null;
		return playedCards[row][col];
	}

	/**
	 * This method plays the card in question using the cardComparator to
	 * determine if cards swap sides.
	 * 
	 * @param cardToPlay
	 *            The card that will be played.
	 * @return A new field with the reflected mutation.
	 */
	public Field playCard(DeployedCard cardToPlay)
	{
		assert cardToPlay != null;
		int row = cardToPlay.row;
		int col = cardToPlay.col;
		assert !this.isCardInPos(row, col);

		Set<DeployedCard> takeOver = this.playFunc.updateField(this,
				cardToPlay, this.cardComparator);
		DeployedCard[][] newPlayedCards = this.copyPlayedCards();
		newPlayedCards[row][col] = cardToPlay;
		for (DeployedCard card : takeOver)
		{
			DeployedCard newCard = card
					.setPlayer(cardToPlay.card.holdingPlayer);
			newPlayedCards[newCard.row][newCard.col] = newCard;
		}

		return new Field(newPlayedCards, this.cardComparator, this.playFunc);
	}

	protected DeployedCard[][] copyPlayedCards()
	{
		DeployedCard[][] result = new DeployedCard[3][3];
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
			{
				result[row][col] = this.playedCards[row][col];
			}

		return result;
	}

	/**
	 * Determine if there is a card at row/col. row/col do not need to be in
	 * bounds.
	 * 
	 * @param row
	 *            The row to check.
	 * @param col
	 *            The col to check.
	 * @return Whethere there is a card at row/col.
	 */
	public boolean isCardInPos(int row, int col)
	{
		return this.isInBounds(row, col) && this.playedCards[row][col] != null;
	}

	/**
	 * This method returns whether the input destination is in bounds (both >= 0
	 * and < 3).
	 * 
	 * @param row
	 *            The row to check.
	 * @param col
	 *            The column to check.
	 * @return Whether the row/column pair is in bounds.
	 */
	public boolean isInBounds(int row, int col)
	{
		return row >= 0 && row < 3 && col >= 0 && col < 3;
	}

	/**
	 * This method returns a function that returns a list of all cards under the
	 * input player's control.
	 * 
	 * @return
	 */
	public Function<Player, ArrayList<UndeployedCard>> getCardsUnderPlayers()
	{
		EnumMap<Player, ArrayList<UndeployedCard>> result = new EnumMap<>(Player.class);
		result.put(Player.SELF, new ArrayList<>());
		result.put(Player.OPPONENT, new ArrayList<>());
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				DeployedCard card = this.getCard(row, col);
				if (card != null)
				{
					result.get(card.card.holdingPlayer).add(card.card);
				}
			}
		}
		
		return result::get;
	}
		
		public Function<Player, Integer> getVictoryPoints()
		{
			EnumMap<Player, Integer> playerPoints = new EnumMap<>(Player.class);
			playerPoints.put(Player.SELF, 0);
			playerPoints.put(Player.OPPONENT, 0);
			for (int row = 0; row < 3; row++)
			{
				for (int col = 0; col < 3; col++)
				{
					DeployedCard card = this.getCard(row, col);
					if (card != null)
					{
						playerPoints.compute(card.card.holdingPlayer, (play, num) -> num + 1);
					}
				}
			}

		return playerPoints::get;
	}
}
