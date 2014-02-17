package com.demigodsrpg.demigods.engine.data.sql;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.util.Configs;
import org.bukkit.configuration.Configuration;

import java.nio.file.AccessDeniedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class SQLDataManager extends SQLSerializer
{
	private Connection connection;

	abstract boolean loadDriver();

	abstract String getDriverClassPath();

	abstract int getDefaultPort();

	abstract String buildURL(String host, int port, String database);

	@Override
	protected boolean preInit()
	{
		if(loadDriver())
		{
			readyConfig();
			connection = readyConnection();
		}
		return false;
	}

	@Override
	protected void init()
	{

	}

	@Override
	protected void save()
	{

	}

	@Override
	protected void flushData() throws AccessDeniedException
	{

	}

	private void readyConfig()
	{
		Configuration config = DemigodsPlugin.getInst().getConfig();
		Configuration defaults = config.getDefaults();
		defaults.set("saving.sql.host", "localhost");
		defaults.set("saving.sql.port", getDefaultPort());
		defaults.set("saving.sql.database", "demigods");
		defaults.set("saving.sql.user", "minecraft");
		defaults.set("saving.sql.password", "minecraft");
		defaults.set("saving.sql.ssl", false);
		config.setDefaults(defaults);
		config.options().copyDefaults(true);
		DemigodsPlugin.getInst().saveConfig();
	}

	private Connection readyConnection()
	{
		// Build the url
		String url = buildURL(Configs.getSettingString("saving.sql.host"), Configs.getSettingInt("saving.sql.port"), Configs.getSettingString("saving.sql.database"));

		// Define the properties
		Properties properties = new Properties();
		properties.setProperty("user", Configs.getSettingString("saving.sql.user"));
		properties.setProperty("password", Configs.getSettingString("saving.sql.password"));
		properties.setProperty("ssl", String.valueOf(Configs.getSettingBoolean("saving.sql.ssl")));

		// Create and return connection
		try
		{
			return DriverManager.getConnection(url, properties);
		}
		catch(SQLException sql)
		{
			sql.printStackTrace();
		}
		return null;
	}

	void createTables() throws SQLException
	{

	}
}
