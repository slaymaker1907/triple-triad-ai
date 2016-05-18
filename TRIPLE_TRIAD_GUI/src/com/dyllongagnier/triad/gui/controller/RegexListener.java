package com.dyllongagnier.triad.gui.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RegexListener
{
	private static class RegexFuncPair
	{
		private Predicate<String> matches;
		private Consumer<String> func;
		
		public RegexFuncPair(String pattern, Consumer<String> function)
		{
			this.func = function;
			matches = Pattern.compile(pattern).asPredicate();
		}
		
		public boolean checkString(String input)
		{
			if (this.matches.test(input))
			{
				this.func.accept(input);
				return true;
			}
			else
				return false;
		}
	}
	
	private Set<RegexFuncPair> listeningList = new HashSet<>();
	private List<String> uncategorizedInput = new ArrayList<>();
	
	public void registerRegex(String pattern, Consumer<String> function)
	{
		synchronized(this.listeningList)
		{
			RegexFuncPair newPair = new RegexFuncPair(pattern, function);
			for(String input : this.uncategorizedInput)
				if (newPair.checkString(input))
					return;
			this.listeningList.add(new RegexFuncPair(pattern, function));
		}
	}
	
	public void receiveString(String input)
	{
		synchronized(listeningList)
		{
			RegexFuncPair toRemove = null;
			for(RegexFuncPair listener : this.listeningList)
				if (listener.checkString(input))
					toRemove = listener;
			if (toRemove != null)
				this.listeningList.remove(toRemove);
			else
				this.uncategorizedInput.add(input);
		}
	}
}
