package com.dyllongagnier.triad.core.functions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.core.Field;

/**
 * This class represents a function that is used to determine which cards are
 * captured when a card is played.
 */
@FunctionalInterface
public interface CardPlayFunction
{
	/**
	 * This is an internal class that will sum together the relevant stats of
	 * two cards and store the second card as as it relates to the first one.
	 * cardsToCapture can then be called to see get cards that were captured.
	 */
	static class SumPairCollection
	{
		private final Map<Integer, Set<DeployedCard>> mapOfSums;

		/**
		 * Initializes an empty sum pair collection.
		 */
		public SumPairCollection()
		{
			this.mapOfSums = new HashMap<>();
		}

		/**
		 * This method adds the specified pair to this object (mapping the sum
		 * to the other card).
		 * 
		 * @param playedCard
		 *            The card just played.
		 * @param otherCard
		 *            An adjacent card.
		 * @return True if the pair was actually added.
		 */
		public boolean addPair(DeployedCard playedCard, DeployedCard otherCard)
		{
			if (otherCard == null)
				return false;
			assert playedCard.cardAdjacent(otherCard);

			int sum = CardPlayFunction.computeSum(playedCard, otherCard);
			if (!this.mapOfSums.containsKey(sum))
				this.mapOfSums.put(sum, new HashSet<DeployedCard>());
			this.mapOfSums.get(sum).add(otherCard);
			return true;
		}

		/**
		 * This method returns the resultant cards captured.
		 * 
		 * @return Captured cards.
		 */
		public Set<DeployedCard> cardsToCapture()
		{
			Set<DeployedCard> result = new HashSet<DeployedCard>();
			for (Entry<Integer, Set<DeployedCard>> pair : this.mapOfSums
					.entrySet())
			{
				if (pair.getValue().size() >= 2)
					result.addAll(this.mapOfSums.get(pair.getKey()));
			}
			return result;
		}
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public Set<DeployedCard> updateField(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator);

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes neither the Plus nor the Same rule.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> basicCapture(Field field,
			DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = new HashSet<>(4);
		int row = toPlay.row;
		int col = toPlay.col;
		tryCapture(toPlay, field.getCard(row - 1, col), cardComparator, result);
		tryCapture(toPlay, field.getCard(row + 1, col), cardComparator, result);
		tryCapture(toPlay, field.getCard(row, col - 1), cardComparator, result);
		tryCapture(toPlay, field.getCard(row, col - 1), cardComparator, result);

		return result;
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This function assumes the Same rule but not the Combo or Plus rule.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> sameCapture(Field field,
			DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.basicCapture(field, toPlay,
				cardComparator);
		Set<DeployedCard> sameCards = CardPlayFunction.basicCapture(field,
				toPlay, DeployedCardComparator::equalCompare);
		if (sameCards.size() >= 2)
			result.addAll(sameCards);
		return result;
	}

	/**
	 * This is an internal function that computes the sum of the played card and
	 * the other card.
	 * 
	 * @param toPlay
	 *            The card just played. Must be non-null.
	 * @param otherCard
	 *            The other card. Must be non-null.
	 * @return The sum of the two cards.
	 */
	static int computeSum(DeployedCard toPlay, DeployedCard otherCard)
	{
		assert toPlay.cardAdjacent(otherCard);
		assert toPlay != null;
		assert otherCard != null;

		DeployedCard.Direction dir = toPlay.getDirectionOfOther(otherCard);
		switch (dir)
		{
			case NORTH:
				return toPlay.card.north + otherCard.card.south;
			case EAST:
				return toPlay.card.east + otherCard.card.west;
			case SOUTH:
				return toPlay.card.south + otherCard.card.north;
			case WEST:
				return toPlay.card.west + toPlay.card.east;
			default:
				throw new RuntimeException("Unsupported enum value.");
		}
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes the Plus rule, but not the Combo or Same rules.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> plusCapture(Field field,
			DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.basicCapture(field, toPlay,
				cardComparator);
		int row = toPlay.row;
		int col = toPlay.col;

		// This could maybe be a little better.
		SumPairCollection sums = new SumPairCollection();
		sums.addPair(toPlay, field.getCard(row + 1, col));
		sums.addPair(toPlay, field.getCard(row - 1, col));
		sums.addPair(toPlay, field.getCard(row, col + 1));
		sums.addPair(toPlay, field.getCard(row, col - 1));
		result.addAll(sums.cardsToCapture());

		return result;
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes the Plus and Combo rules but not the Same rule.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> plusCombo(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> capturedThroughBattle = CardPlayFunction
				.basicCapture(field, toPlay, cardComparator);
		int row = toPlay.row;
		int col = toPlay.col;

		// This could maybe be a little better.
		SumPairCollection sums = new SumPairCollection();
		sums.addPair(toPlay, field.getCard(row + 1, col));
		sums.addPair(toPlay, field.getCard(row - 1, col));
		sums.addPair(toPlay, field.getCard(row, col + 1));
		sums.addPair(toPlay, field.getCard(row, col - 1));
		Set<DeployedCard> capturedThroughRule = sums.cardsToCapture();
		for (DeployedCard card : capturedThroughRule)
		{
			capturedThroughBattle.addAll(CardPlayFunction
					.recursiveNormalCapture(field, card, cardComparator));
		}
		capturedThroughBattle.addAll(capturedThroughRule);
		return capturedThroughBattle;
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes the Same and Combo rules but not not the Plus rule.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> sameCombo(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> capturedThroughBattle = CardPlayFunction
				.basicCapture(field, toPlay, cardComparator);

		Set<DeployedCard> sameCards = CardPlayFunction.basicCapture(field,
				toPlay, DeployedCardComparator::equalCompare);
		if (sameCards.size() >= 2)
		{
			for (DeployedCard card : sameCards)
			{
				capturedThroughBattle.addAll(CardPlayFunction
						.recursiveNormalCapture(field, card, cardComparator));
			}
			capturedThroughBattle.addAll(sameCards);
		}

		return sameCards;
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes the Same, Plus, and Combo rules.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> samePlusCombo(Field field,
			DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		// Take the union of the two sets.
		Set<DeployedCard> result = CardPlayFunction.sameCombo(field, toPlay,
				cardComparator);
		result.addAll(CardPlayFunction.plusCombo(field, toPlay, cardComparator));

		return result;
	}

	/**
	 * This method takes in a field, a card to play, and card comparator and
	 * uses them to determine which cards to capture, returning those as a set.
	 * This method assumes the Same and Plus rules, but not the Combo rule.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played (but not yet added to the state of the
	 *            game).
	 * @param cardComparator
	 *            The comparator to use for comparing card values.
	 * @return The captured cards.
	 */
	public static Set<DeployedCard> samePlus(Field field, DeployedCard toPlay,
			DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = CardPlayFunction.plusCapture(field, toPlay,
				cardComparator);
		result.addAll(CardPlayFunction.sameCapture(field, toPlay,
				cardComparator));
		return result;
	}

	/**
	 * This method is identical to basicCapture except that it will recurse over
	 * captured cards and return cards those cards would capture.
	 * 
	 * @param field
	 *            The current state of the game.
	 * @param toPlay
	 *            The card just played.
	 * @param cardComparator
	 *            The comparator to use for comparing cards.
	 * @return A set of captured cards by toPlay.
	 */
	static Set<DeployedCard> recursiveNormalCapture(Field field,
			DeployedCard toPlay, DeployedCardComparator cardComparator)
	{
		Set<DeployedCard> result = new HashSet<>();
		Set<DeployedCard> captured = CardPlayFunction.basicCapture(field,
				toPlay, cardComparator);
		for (DeployedCard card : captured)
		{
			result.addAll(CardPlayFunction.basicCapture(field, card,
					cardComparator));
		}
		result.addAll(captured);
		return result;
	}

	/**
	 * This method trys to capture a card if it is non-null.
	 * 
	 * @param justPlayed
	 *            The card just played.
	 * @param toCapture
	 *            The card to try and capture. This may be null.
	 * @param cardComparator
	 *            The comparator to use for comparing the two cards.
	 * @param result
	 *            The set to add toCapture to if justPlayed captures toCapture.
	 */
	static void tryCapture(DeployedCard justPlayed, DeployedCard toCapture,
			DeployedCardComparator cardComparator, Set<DeployedCard> result)
	{
		if (toCapture == null)
			return;
		assert justPlayed != null;
		assert justPlayed.cardAdjacent(toCapture);
		if (cardComparator.apply(justPlayed, toCapture))
			result.add(toCapture);
	}
}
