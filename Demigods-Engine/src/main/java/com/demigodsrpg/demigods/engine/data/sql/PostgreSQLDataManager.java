package com.demigodsrpg.demigods.engine.data.sql;

import com.demigodsrpg.demigods.engine.util.Messages;

public class PostgreSQLDataManager extends SQLDataManager
{
	@Override boolean loadDriver()
	{
		try
		{
			Class.forName(getDriverClassPath());
		}
		catch(ClassNotFoundException notFound)
		{
			Messages.warning("PostgreSQL's JBDC Driver was not found.  Defaulting to the file save method.");
			return false;
		}
		return true;
	}

	@Override String getDriverClassPath()
	{
		return "org.postgresql.Driver";
	}

	@Override int getDefaultPort()
	{
		return 5432;
	}

	@Override String buildURL(String host, int port, String database)
	{
		return String.format("jbdc:postgresql://%s:%d/%s", host, port, database);
	}
}
