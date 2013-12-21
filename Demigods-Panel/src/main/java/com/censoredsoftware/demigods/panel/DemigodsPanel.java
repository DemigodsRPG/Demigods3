package com.censoredsoftware.demigods.panel;

import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class DemigodsPanel extends JavaPlugin
{
	private static Server SERVER;
    private static DemigodsPanel WEB_PANEL;

    /**
     * The Bukkit enable method.
     */
    @Override
    public void onEnable()
    {
        // Start panel if allowed
        if(Configs.getSettingBoolean("panel.use") && Bukkit.getPluginManager().getPlugin("CensoredLib-Web") != null)
        {
            WEB_PANEL = this;
            runServer();
        }
    }

    /**
     * The Bukkit disable method.
     */
    @Override
    public void onDisable()
    {
        // Stop panel if allowed
        if(Configs.getSettingBoolean("panel.use"))
        {
            // TODO Add other related methods here as needed
            stopServer();
        }
    }

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

		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable()
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

		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable()
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
