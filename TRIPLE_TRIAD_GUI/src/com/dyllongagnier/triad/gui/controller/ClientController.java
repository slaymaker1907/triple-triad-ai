package com.dyllongagnier.triad.gui.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableObject;

import com.dyllongagnier.triad.card.CardList;
import com.dyllongagnier.triad.card.HandFactory;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionRule;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.net.*;
import com.dyllongagnier.triad.net.SetBoolean.*;
import com.dyllongagnier.triad.net.GetBoolean.*;

public class ClientController implements GameController
{
	private Socket sock;
	private AtomicInteger idGen = new AtomicInteger(0);
	private OutputStreamWriter writer;
	private Scanner reader;
	private RegexListener listener = new RegexListener();
	
	protected int getNewId()
	{
		return idGen.incrementAndGet();
	}
	
	protected void sendString(String toSend)
	{
		try
		{
			writer.write(toSend);
			writer.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Sends message to listener.
	protected void getNextMessage()
	{
		this.listener.receiveString(reader.next());
	}
	
	// Pattern is a regex describing what we are waiting for.
	protected void awaitMessage(String pattern, Consumer<String> callback)
	{
		this.listener.registerRegex(pattern, callback);
	}
	
	public ClientController(String address, int port) throws UnknownHostException, IOException
	{
		sock = new Socket(address, port);
		writer = new OutputStreamWriter(sock.getOutputStream());
		reader = new Scanner(sock.getInputStream());
		reader.useDelimiter("\r\n\r\n");
	}

	@Override
	public void setAgent(Player player, boolean isAi)
	{
		// "SET AGENT {player} {isAi}\r\n"
			this.sendString(String.format("SET AGENT %s %s\r\n\r\n", player.toString(), Boolean.toString(isAi)));
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player, String filename)
			throws FileNotFoundException
	{
		// "SET PLAYER_DECK {player}\r\n{cards}\r\n\r\n"
		// Each card is on its own line.
		UndeployedCard[] result = HandFactory.getDeck(player, filename, null);
		this.sendDeck(player, result);
		return Arrays.asList(result);
	}
	
	private String serializeDeck(UndeployedCard[] cards)
	{
		ArrayList<String> toJoin = new ArrayList<>();
		for(UndeployedCard card : cards)
			toJoin.add(card.deploy().name);
		return String.join("\r\n", toJoin);
	}
	
	private void sendDeck(Player player, UndeployedCard[] cards)
	{
		this.sendString(String.format("SET PLAYER_DECK %s\r\n%s\r\n\r\n", player.toString(), this.serializeDeck(cards)));
	}

	@Override
	public Iterable<UndeployedCard> setPlayerDeck(Player player,
			String[] cardNames)
	{
		// See above.
		UndeployedCard[] result = CardList.generateHand(player, cardNames);
		this.sendDeck(player, result);
		return Arrays.asList(result);
	}
	
	// Sets a simple boolean.
	private void setBoolean(String name, boolean value)
	{
		this.sendString(String.format("SET %s %s\r\n\r\n", name, Boolean.toString(value)));
	}

	@Override
	public void setIsCombo(boolean isCombo)
	{
		// SET IS_COMBO {isCombo}\r\n"
		this.setBoolean("IS_COMBO", isCombo);
	}

	@Override
	public void setIsFallenAce(boolean isFallenAce)
	{
		// SET IS_FALLEN_ACE {isFallenAce}
		this.setBoolean("IS_FALLEN_ACE", isFallenAce);
	}

	@Override
	public void setIsPlus(boolean isPlus)
	{
		// SET IS_PLUS {isPlus}
		this.setBoolean("IS_PLUS", isPlus);
	}

	@Override
	public void setIsReverse(boolean isReverse)
	{
		// SET IS_REVERSE {isReverse}
		this.setBoolean("IS_REVERSE", isReverse);
	}

	@Override
	public void setIsSame(boolean isSame)
	{
		// SET IS_SAME {isSame}
		this.setBoolean("IS_SAME", isSame);
	}

	@Override
	public void setAscensionRule(AscensionRule rule)
	{
		// SET ASCENSCION_RULE {rule}
		this.sendString(String.format("SET ASCENSION_RULE %s", rule.toString()));
	}

	@Override
	public void setIsOrder(boolean isOrder)
	{
		// SET IS_ORDER {isOrder}
		this.setBoolean("IS_ORDER", isOrder);
	}

	@Override
	public void setIsSuddenDeath(boolean isSuddenDeath)
	{
		// SET IS_SUDDEN_DEATH {isSuddenDeath}
		this.setBoolean("IS_SUDDEN_DEATH", isSuddenDeath);
	}
	
	// Gets a boolean according to the protocol.
	private boolean getBoolean(String name)
	{
		return Boolean.valueOf(this.getString(name));
	}
	
	// Generic getter with the protocol.
	private String getString(String name)
	{
		int functionId = this.getNewId();
		this.sendString(String.format("GET %s %i\r\n\r\n", name, functionId));
		String pattern = String.format("RESP %i (.*)\r\n\r\n", functionId);
		MutableObject<String> result = new MutableObject<>(null);
		Consumer<String> callback = (input) ->
		{
			result.setValue(Pattern.compile(pattern).matcher(input).group(1));
		};
		this.awaitMessage("", callback);
		while(result.getValue() == null)
			this.getNextMessage();
		return result.getValue();
	}

	@Override
	public boolean getIsOrder()
	{
		// GET IS_ORDER
		return this.getBoolean("IS_ORDER");
	}

	@Override
	public void setDefaultOptions()
	{
		// SET DEFAULT
		this.sendString("SET DEFAULT\r\n\r\n");
	}

	@Override
	public void setMaxThreads(int maxThreads)
	{
		// SET MAX_THREADS {maxThreads}
		this.sendString(String.format("SET MAX_THREADS %i\r\n\r\n", maxThreads));
	}

	@Override
	public int getMaxThreads()
	{
		// GET MAX_THREADS		
		return Integer.valueOf(this.getString("MAX_THREADS"));
	}

	@Override
	public boolean getIsCombo()
	{
		// GET IS_COMBO
		return this.getBoolean("IS_COMBO");
	}

	@Override
	public boolean getIsFallenAce()
	{
		// GET IS_FALLEN_ACE
		return this.getBoolean("IS_FALLEN_ACE");
	}

	@Override
	public boolean getIsPlus()
	{
		// GET IS_PLUS
		return this.getBoolean("IS_PLUS");
	}

	@Override
	public boolean getIsReverse()
	{
		// GET IS_REVERSE
		return this.getBoolean("IS_REVERSE");
	}

	@Override
	public boolean getIsSame()
	{
		// GET IS_SAME
		return this.getBoolean("IS_SAME");
	}

	@Override
	public boolean getIsSuddenDeath()
	{
		// GET_IS_SUDDEN_DEATH
		return this.getBoolean("IS_SUDDEN_DEATH");
	}

	@Override
	public double getTimeout()
	{
		// GET TIMEOUT
		return Double.valueOf(this.getString("TIMEOUT"));
	}

	@Override
	public AscensionRule getAscensionRule()
	{
		// GET ASCENSION_RULE
		return AscensionRule.valueOf(this.getString("ASCENSION_RULE"));
	}

	@Override
	public void verifyOptionValidity()
	{
		// GET IS_VALID
		if (!this.getBoolean("IS_VALID"))
			throw new IllegalArgumentException("Options aren't valid.");
	}

	@Override
	public void setTimeout(double timeout)
	{
		// SET TIMEOUT
		this.sendString(String.format("SET TIMEOUT %d\r\n\r\n", timeout));
	}

	@Override
	public TriadGame startNewGame()
	{
		// START_GAME
		// Also returns a game serialization
		this.socket.sendObject(new StartNewGame());
		try
		{
			return ((StartNewGame)this.socket.getLastObject().get()).getNewGame();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void makeMove(PossibleMove move)
	{
		// MOVE {id} {moveSer}
		// Returns SUCCESS|FAILURE
		this.sendString(String.format("MOVE %i %s\r\n\r\n", this.getNewId(), serializeMove(move)));
	}
	
	private String serializeMove(PossibleMove move)
	{
		return String.format("(%i, %i) %s", move.row, move.col, move.toPlay.deploy().name);
	}

	@Override
	public boolean getIsAI(Player player)
	{
		// GET IS_AI_{player}
		// Each request that requires response will have an initial id number.
		return this.getBoolean("IS_AI_" + player.toString());
	}
}
