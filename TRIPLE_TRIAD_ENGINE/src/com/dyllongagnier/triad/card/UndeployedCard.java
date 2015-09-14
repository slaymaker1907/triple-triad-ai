package com.dyllongagnier.triad.card;

/**
 * This interface is provided so that players/npcs can have hidden cards as well in their hands.
 * The engine hashes these objects, so implementing classes should override the hashCode and
 * equals methods. The Card returned by this method should 
 */
public interface UndeployedCard 
{
	/**
	 * This method is only guaranteed to return a valid card once. Subsequent calls have no
	 * guarantees about anything unless isVisible returns true.
	 * @return The card to play.
	 */
	public Card deploy();
	
	/**
	 * If this method returns true, then it is deploy is safe to call multiple times, even when a card
	 * is in the hand. Typically, this will only be the case for the Card class.
	 * @return True if deploy may be called multiple times.
	 */
	public default boolean isVisible()
	{
		return false;
	}
}
