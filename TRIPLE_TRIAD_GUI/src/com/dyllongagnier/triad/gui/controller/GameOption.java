package com.dyllongagnier.triad.gui.controller;

import java.io.Serializable;

public class GameOption implements Serializable
{
	private static final long serialVersionUID = 1L;
	public final Serializable input;
	public final int option;
	
	public static final int PLAYER_AGENT = 0;
	public static final int SET_DECK = 1;
	public static final int IS_COMBO = 2;
	public static final int IS_FALLEN_ACE = 3;
	public static final int IS_PLUS = 4;
	public static final int IS_REVERSE = 5;
	public static final int IS_SAME = 6;
	public static final int ASCENSION = 7;
	public static final int IS_ORDER = 8;
	public static final int IS_SUDDEN_DEATH = 9;
	public static final int DEFAULT_OPTS = 10;
	public static final int MAX_THREADS = 11;
	public static final int MAX_TIME = 12;
	public static final int VERIFY_OPTIONS = 13;
	public static final int START_GAME = 14;
	public static final int MAKE_MOVE = 15;
	
	public GameOption(int option, Serializable input)
	{
		this.input = input;
		this.option = option;
	}
}
