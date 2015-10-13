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

	public static TriadGame gameFactory(PlayerSupplier firstPlayerGen,
			BoardState.Builder gameBuilder, GameAgent selfAgent,
			GameAgent opponentAgent, GameListener listener)
	{
		assert selfAgent != null;
		assert opponentAgent != null;
		
		if (gameBuilder.isSuddenDeath)
			return new SuddenDeathGame(firstPlayerGen, gameBuilder, selfAgent,
					opponentAgent, listener);
		else
			return new TriadGame(firstPlayerGen.get(), gameBuilder, selfAgent,
					opponentAgent, listener);
	}

	public TriadGame(Player firstPlayer, BoardState.Builder gameBuilder,
			GameAgent selfAgent, GameAgent opponentAgent, GameListener listener)
	{
		this.init(firstPlayer, gameBuilder);

		this.gameAgentMap = new EnumMap<>(Player.class);
		this.gameAgentMap.put(Player.SELF, selfAgent);
		this.gameAgentMap.put(Player.OPPONENT, opponentAgent);

		this.moveValidator = gameBuilder.getMoveValidator();
		this.listener = listener;
	}

	// This can be used to reset the game to turn 1.
	protected void init(Player firstPlayer, BoardState.Builder builder)
	{
		assert firstPlayer != Player.NONE;

		this.currentPlayer = firstPlayer;
		this.turnCount = 1;
		this.currentState = builder.build(firstPlayer);
	}

	public BoardState getCurrentState()
	{
		return this.currentState;
	}

	public int getTurnCount()
	{
		return this.turnCount;
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
		this.currentState = this.currentState.playCard(this.currentPlayer,
				card, row, col);

		this.changeTurn();
	}

	protected void changeTurn()
	{
		this.currentPlayer = this.currentPlayer.swapPlayer();
		this.turnCount++;
		if (this.turnCount > 9)
		{
			this.completeGame();
		} else
		{
			this.listener.gameChanged(this);
			this.startTurn();
		}
	}

	protected void completeGame()
	{
		assert this.currentState.gameComplete();
		this.listener.gameComplete(this);
	}

	/**
	 * This method retrieves all possible valid moves.
	 * 
	 * @return A list with all possible valid moves.
	 */
	public List<PossibleMove> getValidMoves()
	{
		ArrayList<PossibleMove> result = new ArrayList<>();
		for (UndeployedCard card : this.currentState
				.getHand(this.currentPlayer))
			for (int row = 0; row < 3; row++)
				for (int col = 0; col < 3; col++)
					if (this.moveValidator.apply(this.currentState, card,
							this.currentPlayer, row, col))
						result.add(new PossibleMove(card, row, col));

		return result;
	}
	
	public boolean isValidMove(PossibleMove move)
	{
		return this.moveValidator.apply(this.getCurrentState(), move.toPlay, this.getCurrentPlayer(), move.row, move.col);
	}

	protected TriadGame(TriadGame oldGame, GameListener newListener)
	{
		this.currentPlayer = oldGame.currentPlayer;
		this.currentState = oldGame.currentState.clone();
		this.turnCount = oldGame.turnCount;
		this.gameAgentMap = new EnumMap<>(Player.class);
		this.gameAgentMap.put(Player.SELF, oldGame.getGameAgent(Player.SELF)
				.clone());
		this.gameAgentMap.put(Player.OPPONENT,
				oldGame.getGameAgent(Player.OPPONENT).clone());
		this.listener = newListener;
		this.moveValidator = oldGame.moveValidator;
	}

	// This method does not clone the observer.
	@Override
	public TriadGame clone()
	{
		return new TriadGame(this, TriadGame.defaultListener);
	}

	public TriadGame clone(GameListener listener)
	{
		return new TriadGame(this, listener);
	}
}
