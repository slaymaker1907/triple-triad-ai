package com.dyllongagnier.triad.card;

/**
 * This interface is provided so that players/npcs can have hidden cards as well in their hands.
 * The engine hashes these objects, so implementing classes should override the hashCode and
 * equals methods.
 */
@FunctionalInterface
public interface UndeployedCard 
{
	/**
	 * This method is only guaranteed to return a valid card once. Subsequent calls have no
	 * guarantees about anything.
	 * @return The card to play.
	 */
	public Card deploy();
}
