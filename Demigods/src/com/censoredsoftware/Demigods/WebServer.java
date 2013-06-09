package com.censoredsoftware.Demigods;

import org.eclipse.jetty.server.Server;

public class WebServer
{
	public static void main(String[] args) throws Exception
	{
		Server server = new Server(9900);
		server.start();
		server.join();
	}
}
