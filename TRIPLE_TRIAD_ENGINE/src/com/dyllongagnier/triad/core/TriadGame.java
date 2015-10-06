package com.dyllongagnier.triad.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.functions.MoveValidator;

public class TriadGame
{
	protected static final GameListener defaultListener = new DefaultListener();
	
	public final MoveValidator moveValidator;
	
	protected Player currentPlayer;
	protected BoardState currentState;
	protected int turnCount;
	
	private final EnumMap<Player, GameAgent> gameAgentMap;
	protected final GameListener listener;
	
	public TriadGame(Player firstPlayer,
			BoardState.Builder gameBuilder,
			GameAgent selfAgent, GameAgent opponentAgent, GameListener listener)
	{
		assert firstPlayer != Player.NONE;
		
		this.turnCount = 1;
		this.currentPlayer = firstPlayer;
		
		this.gameAgentMap = new EnumMap<>(Player.class);
		this.gameAgentMap.put(Player.SELF, selfAgent);
		this.gameAgentMap.put(Player.OPPONENT, opponentAgent);
		
		this.currentState = gameBuilder.build();
		this.moveValidator = gameBuilder.getMoveValidator();
		this.listener = listener;
	}
	
	public BoardState getCurrentState()
	{
		return this.currentState;
	}
	
	public Player getCurrentPlayer()
	{
		return this.currentPlayer;
	}
	
	public void startGame()
	{
		this.startTurn();
	}
	
	protected GameAgent getGameAgent(Player player)
	{
		assert player != Player.NONE;
		return this.gameAgentMap.get(player);
	}
		
	protected GameAgent startTurn()
	{
		GameAgent agent = this.getGameAgent(this.currentPlayer);
		agent.takeTurn(this);
		return agent;
	}
	
	public void takeTurn(UndeployedCard card, int row, int col)
	{
		if (!this.moveValidator.apply(this.currentState, card,
				this.currentPlayer, row, col))
			throw new IllegalArgumentException();
		this.currentState = this.currentState.playCard(this.currentPlayer, card, row, col);
		
		this.changeTurn();
	}
	
	protected void changeTurn()
	{
		this.currentPlayer = this.currentPlayer.swapPlayer();
		this.turnCount++;
		if (this.turnCount > 9)
		{
			assert this.currentState.gameComplete();
			this.listener.gameComplete(this);
		}
		else
		{
			this.listener.gameChanged(this);
			this.startTurn();
		}
	}
	
	/**
	 * This method retrieves all possible valid moves.
	 * @return A list with all possible valid moves.
	 */
	public List<PossibleMove> getValidMoves()
	{
		ArrayList<PossibleMove> result = new ArrayList<>();
		for(UndeployedCard card : this.currentState.getHand(this.currentPlayer))
			for(int row = 0; row < 3; row++)
				for(int col = 0; col < 3; col++)
					if (this.moveValidator.apply(this.currentState, card, this.currentPlayer, row, col))
						result.add(new PossibleMove(card, row, col));
		
		return result;
	}
	
	protected TriadGame(TriadGame oldGame)
	{
		this.currentPlayer = oldGame.currentPlayer;
		this.currentState = oldGame.currentState.clone();
		this.turnCount = oldGame.turnCount;
		this.gameAgentMap = oldGame.gameAgentMap;
		this.listener = TriadGame.defaultListener;
		this.moveValidator = oldGame.moveValidator;
	}
	
	// This method does not clone the observer.
	@Override
	public TriadGame clone()
	{
		return new TriadGame(this);
	}
	
	public static class PossibleMove
	{
		public final UndeployedCard toPlay;
		public final int row, col;
		
		public PossibleMove(UndeployedCard toPlay, int row, int col)
		{
			this.toPlay = toPlay;
			this.row = row;
			this.col = col;
		}
	}
}
