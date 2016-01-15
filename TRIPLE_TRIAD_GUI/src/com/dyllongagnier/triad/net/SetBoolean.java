package com.dyllongagnier.triad.net;

public abstract class SetBoolean extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private boolean value;
	
	protected SetBoolean(boolean value)
	{
		this.setValue(value);
	}
	
	private void setValue(boolean value)
	{
		this.value = value;
	}
	
	protected boolean getValue()
	{
		return this.value;
	}
	
	public static class SetIsCombo extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsCombo(boolean isCombo)
		{
			super(isCombo);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsCombo(this.getValue());
		}
	}
	
	public static class SetIsFallenAce extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsFallenAce(boolean isFallenAce)
		{
			super(isFallenAce);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsFallenAce(this.getValue());
		}
	}
	
	public static class SetIsPlus extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsPlus(boolean isPlus)
		{
			super(isPlus);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsPlus(this.getValue());
		}
	}
	
	public static class SetIsReverse extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsReverse(boolean isReverse)
		{
			super(isReverse);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsReverse(this.getValue());
		}
	}
	
	public static class SetIsSame extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsSame(boolean isSame)
		{
			super(isSame);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsSame(this.getValue());
		}
	}
	
	public static class SetIsOrder extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsOrder(boolean isOrder)
		{
			super(isOrder);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsOrder(this.getValue());
		}
	}
	
	public static class SetIsSuddenDeath extends SetBoolean
	{
		private static final long serialVersionUID = 1L;

		public SetIsSuddenDeath(boolean isSuddenDeath)
		{
			super(isSuddenDeath);
		}
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			server.getController().setIsSuddenDeath(this.getValue());
		}
	}
}
