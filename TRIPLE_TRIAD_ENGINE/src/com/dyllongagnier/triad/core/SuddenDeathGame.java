package com.dyllongagnier.triad.core;

import java.util.function.Function;
import java.util.function.Supplier;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;

public class SuddenDeathGame extends TriadGame
{
	private final PlayerSupplier firstPlayerGen;
	private final BoardState.Builder builder;
	
	public SuddenDeathGame(PlayerSupplier firstPlayerGen, BoardState.Builder builder, GameAgent selfAgent, GameAgent otherAgent,
			GameListener listener)
	{
		super(firstPlayerGen.get(), builder, selfAgent, otherAgent, listener);
		this.firstPlayerGen = firstPlayerGen;
		this.builder = builder;
	}
	
	protected SuddenDeathGame(TriadGame game, GameListener listener, PlayerSupplier firstPlayerGen, BoardState.Builder builder)
	{
		super(game, listener);
		this.firstPlayerGen = firstPlayerGen.clone();
		this.builder = builder; // This should be safe for now, no need for defensive copying since only hand is mutated.
	}
	
	@Override
	public TriadGame clone()
	{
		return new SuddenDeathGame(this, TriadGame.defaultListener, this.firstPlayerGen, this.builder);
	}
	
	@Override
	public TriadGame clone(GameListener listener)
	{
		return new SuddenDeathGame(this, listener, this.firstPlayerGen, this.builder);
	}
	
	@Override
	public void completeGame()
	{
		Player winner = this.getCurrentState().getWinner();
		if (winner != Player.NONE)
		{
			super.completeGame();
		}
		else
		{
			// No winner so need to set up new game.
			Function<Player, UndeployedCard[]> cardFunc = this.getCurrentState().getCardsUnderPlayers();
			this.builder.setHand(Player.SELF, cardFunc.apply(Player.SELF));
			this.builder.setHand(Player.OPPONENT, cardFunc.apply(Player.OPPONENT));
			this.init(this.firstPlayerGen.get(), this.builder);
		}
	}
}
