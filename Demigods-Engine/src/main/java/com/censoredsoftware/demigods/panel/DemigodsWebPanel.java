package com.censoredsoftware.demigods.panel;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import org.bukkit.Bukkit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class DemigodsWebPanel
{
	private static Server SERVER;

	public void runServer()
	{
		Messages.info("Initializing web-server...");

		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

		context.setContextPath("/");
		context.setResourceBase("webapp");

		context.setClassLoader(Thread.currentThread().getContextClassLoader());

		context.addServlet(DefaultServlet.class, "/");

		SERVER = new Server(Configs.getSettingInt("panel.port"));
		SERVER.setHandler(context);

		Messages.info("Starting web-server...");

		Bukkit.getScheduler().scheduleAsyncDelayedTask(Demigods.PLUGIN, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					SERVER.start();
					SERVER.join();
				}
				catch(Exception e)
				{
					Messages.severe("Web-server interrupted!");
					return;
				}
				Messages.info("Web-server running...");
			}
		}, 0);
	}

	public void stopServer()
	{
		Messages.info("Stopping web-server...");

		Bukkit.getScheduler().scheduleAsyncDelayedTask(Demigods.PLUGIN, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					SERVER.stop();
				}
				catch(Exception e)
				{
					Messages.severe("Web-server interrupted!");
					return;
				}
				Messages.info("Web-server disabled...");
			}
		}, 0);
	}
}
