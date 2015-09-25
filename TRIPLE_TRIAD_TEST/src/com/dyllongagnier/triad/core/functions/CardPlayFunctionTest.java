package com.dyllongagnier.triad.core.functions;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dyllongagnier.triad.card.Card;
import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.core.Field;

public class CardPlayFunctionTest
{
	public static final Card perfectAce = new Card(10,10,10,10,"Ace",Card.Type.NONE, 5,Player.SELF);
	public static final Card lowlyOne = new Card(1,1,1,1,"One",Card.Type.NONE, 5, Player.OPPONENT);
	public static final Field testField = new Field(DeployedCardComparator::regularCompare, CardPlayFunction::basicCapture);
	
	@Test
	public void testBasicCapture()
	{
		Field newField = testField.playCard(new DeployedCard(lowlyOne, 1, 1))
				.playCard(new DeployedCard(perfectAce, 0, 1));
		assertEquals(newField.getCard(1, 1), new DeployedCard(lowlyOne, 1, 1).setPlayer(Player.SELF));
		assertEquals(newField.getCard(0, 1), new DeployedCard(perfectAce, 0, 1));
	}
	
	@Test
	public void testBasicNoCapture()
	{
		Field newField = testField.playCard(new DeployedCard(perfectAce, 1, 1))
				.playCard(new DeployedCard(lowlyOne, 0, 1));
		assertEquals(newField.getCard(0, 1), new DeployedCard(lowlyOne, 0, 1));
		assertEquals(newField.getCard(1, 1), new DeployedCard(perfectAce, 1, 1));
	}
	
	@Test
	public void testBasicNoCaptureRange()
	{
		Field newField = testField.playCard(new DeployedCard(lowlyOne, 1, 1))
				.playCard(new DeployedCard(perfectAce, 0, 0));
		assertEquals(newField.getCard(1, 1), new DeployedCard(lowlyOne, 1, 1));
		assertEquals(newField.getCard(0, 0), new DeployedCard(perfectAce, 0, 0));
	}
	
	@Test
	public void testBasicNoCaptureEqual()
	{
		Field newField = testField.playCard(new DeployedCard(lowlyOne, 1, 1))
				.playCard(new DeployedCard(lowlyOne, 0, 1));
		assertEquals(newField.getCard(1, 1), new DeployedCard(lowlyOne, 1, 1));
		assertEquals(newField.getCard(0, 1), new DeployedCard(lowlyOne, 0, 1));
	}
}
