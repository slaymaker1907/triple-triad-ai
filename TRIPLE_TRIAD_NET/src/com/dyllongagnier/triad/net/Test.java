package com.dyllongagnier.triad.net;

import java.util.concurrent.Future;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		ServerPoint server = new ServerPoint(8000);
		Future<Integer> connec = server.acceptNewConn();
		ObjectSocket client = new ObjectSocket("127.0.0.1", 8000);
		int id = connec.get();
		client.sendObject("Hello! My name is Dyllon.");
		System.out.println(server.getLastObject(id).get());
		server.close();
		client.close();
	}
}
