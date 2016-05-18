package com.dyllongagnier.triad.gui.controller;

public class InvalidPlayerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public InvalidPlayerException()
	{
		super();
	}
	
	public InvalidPlayerException(String message)
	{
		super(message);
	}
}
