package com.dyllongagnier.triad.card;

import java.util.List;

/**
 * This interface is provided so that players/npcs can have hidden cards as well
 * in their hands. The engine uses a sorted set for this object, so implementing
 * classes must implement a comparator and if the Order rule is used, this
 * comparator will also be used to determine the order in which cards can be
 * played. Every UndeployedCard should also implement clone since this is an
 * inherently mutable object.
 */
public interface UndeployedCard extends Comparable<UndeployedCard>
{
	/**
	 * This method is only guaranteed to return a valid card once. Subsequent
	 * calls have no guarantees about anything unless isVisible returns true.
	 * 
	 * @return The card to play.
	 */
	public Card deploy();

	/**
	 * This method returns a list of all possible cards that this card could be.
	 * 
	 * @return A list of all possibilities for this card.
	 */
	public List<ProbCard> getPossibleCards();

	/**
	 * If this method returns true, then it is deploy is safe to call multiple
	 * times, even when a card is in the hand. Typically, this will only be the
	 * case for the Card class.
	 * 
	 * @return True if deploy may be called multiple times.
	 */
	public default boolean isVisible()
	{
		return false;
	}
}
