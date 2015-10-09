package com.dyllongagnier.triad.core;

import java.util.function.Supplier;

import com.dyllongagnier.triad.card.Player;

@FunctionalInterface
public interface PlayerSupplier extends Supplier<Player>
{
	public default PlayerSupplier clone()
	{
		return this;
	}
}
