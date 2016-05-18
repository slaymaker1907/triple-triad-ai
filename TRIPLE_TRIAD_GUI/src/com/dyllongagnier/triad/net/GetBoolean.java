package com.dyllongagnier.triad.net;

public abstract class GetBoolean extends NetworkObject
{
	private static final long serialVersionUID = 1L;
	
	private boolean isComplete;
	private boolean value;
	
	private void setComplete()
	{
		this.isComplete = true;
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	
	public boolean getValue()
	{
		if (!this.isComplete())
			throw new UnsupportedOperationException();
		return this.value;
	}
	
	protected void setValue(boolean value)
	{
		if (this.isComplete())
			throw new UnsupportedOperationException();
		this.value = value;
		this.setComplete();
	}
	
	public GetBoolean()
	{
		this.isComplete = false;
	}

	public static class GetIsOrder extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsOrder());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsCombo extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsCombo());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsFallenAce extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsFallenAce());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsPlus extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsPlus());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsReverse extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsReverse());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsSame extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsSame());
			server.sendMessage(this);
		}
	}
	
	public static class GetIsSuddenDeath extends GetBoolean
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void processObjectServer(TriadServer server)
		{
			this.setValue(server.getController().getIsSuddenDeath());
			server.sendMessage(this);
		}
	}
}
