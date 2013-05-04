package com.censoredsoftware.Modules.Persistence;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.Data.DataModule;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class MySQLPersistenceModule implements PersistenceModule
{
	private Plugin plugin;
	private String database;
	private Map map;
	protected Connections connections;

	protected static Logger log = Logger.getLogger("Minecraft");

	public MySQLPersistenceModule(Plugin instance, String database, String host, String port, String username, String password)
	{
		this.plugin = instance;
		this.database = database;

		this.connections = new Connections(instance, host, port, this.database, username, password);
	}

	@Override
	public boolean save(DataModule dataModule)
	{
		this.connections.createTable("Test", new LinkedList<Column>()
		{
			{
				for(Object key : map.keySet())
				{
					// TODO
				}
				add(new Column("", PossibleDataTypes.DATE));
			}
		});
		return true;
	}

	@Override
	public boolean save(DataStubModule stub)
	{
		return false; // TODO
	}

	@Override
	public boolean save(List stubs)
	{
		return false; // TODO
	}

	@Override
	public void load()
	{
		// TODO
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}

class Connections
{
	private Plugin plugin;
	private String host, port, database, username, password;
	private Connection connection;
	private Statement statement;
	private Boolean connected;

	protected Connections(Plugin instance, String host, String port, String database, String username, String password)
	{
		this.plugin = instance;
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.statement = null;
		this.connected = false;
	}

	protected Connection open()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
			return this.connection;
		}
		catch(Exception e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error: " + e);
		}
		return this.connection;
	}

	protected boolean check()
	{
		return this.connection != null;
	}

	protected Connection get()
	{
		return this.connection;
	}

	protected void close(Connection connection)
	{
		try
		{
			connection.close();
			connection = null;
		}
		catch(Exception ignored)
		{}
	}

	protected boolean connect()
	{
		open();
		try
		{
			this.statement = this.connection.createStatement();
			this.connected = true;
			MySQLPersistenceModule.log.info("[" + plugin.getName() + "] Connection to database successful.");
		}
		catch(Exception e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error: " + e);
		}

		close(this.connection);
		return this.connected;
	}

	protected void createTable(String tableName, LinkedList<Column> columns)
	{
		if(this.connected)
		{
			int count = 0;
			StringBuilder statement = new StringBuilder();
			for(Column column : columns)
			{
				count++;
				statement.append(column.getName() + " " + column.getDataType().toString());
				if(count < columns.size() - 1) statement.append(", ");
			}
			String SQLStatement = statement.toString();

			this.connection = open();
			try
			{
				this.statement = this.connection.createStatement();
				this.statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " ( " + SQLStatement + ");");
			}
			catch(SQLException e)
			{
				MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
				MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error Code: " + e.getErrorCode());
			}
			close(this.connection);
		}
		else MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error: Not connected to database.");
	}

	protected int countEntrys(String tableName)
	{
		this.connection = open();
		try
		{
			this.statement = this.connection.createStatement();
			ResultSet result = this.statement.executeQuery("SELECT COUNT(*) FROM " + tableName + ";");
			result.next();
			close(this.connection);
			return result.findColumn("COUNT(*)");
		}
		catch(SQLException e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error Code: " + e.getErrorCode());
		}
		close(this.connection);
		return -1;
	}

	protected void insertEntry(String tableName, List<Column> columns)
	{
		int count = 0;
		StringBuilder columnData = new StringBuilder();
		StringBuilder valuesData = new StringBuilder();
		for(Column column : columns)
		{
			count++;
			columnData.append("`" + column.getName());
			valuesData.append("'" + column.getValue());
			if(count < columns.size() - 1)
			{
				columnData.append("`,");
				valuesData.append("',");
			}
			else
			{
				columnData.append("`");
				valuesData.append("'");
			}
		}

		this.connection = open();
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate("INSERT INTO `" + this.database + "`.`" + tableName + "` (" + columnData.toString() + ") VALUES (" + valuesData.toString() + ");");
		}
		catch(SQLException e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error Code: " + e.getErrorCode());
		}
		close(this.connection);
	}

	public ResultSet getAllEntrys(String tableName)
	{
		this.connection = open();
		try
		{
			this.statement = this.connection.createStatement();
			return this.statement.executeQuery("SELECT * FROM " + tableName + ";");
		}
		catch(SQLException e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error Code: " + e.getErrorCode());

			close(this.connection);
		}
		return null;
	}

	public ResultSet getSortedEntrys(String tableName, Column column, PossibleOperators operator, Object value)
	{
		this.connection = open();
		try
		{
			this.statement = this.connection.createStatement();
			return this.statement.executeQuery("SELECT * FROM " + tableName + " WHERE `" + column.getName() + "` " + operator.toString() + "'" + value + "';");
		}
		catch(SQLException e)
		{
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Unable to connect to database.");
			MySQLPersistenceModule.log.severe("[" + plugin.getName() + "] Error Code: " + e.getErrorCode());

			close(this.connection);
		}
		return null;
	}
}

enum PossibleOperators
{
	EQUAL_TO("="), GREATER_THAN(">"), LESS_THAN("<"), GREATER_THAN_EQUAL_TO(">="), LESS_THAN_EQUAL_TO("<=");

	private String value;

	private PossibleOperators(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return this.value;
	}
}

enum PossibleDataTypes
{
	STRING("TEXT"), INTEGER("INT"), DOUBLE("DOUBLE"), FLOAT("FLOAT"), BIG_DECIMAL("DECIMAL"), BOOLEAN("BOOLEAN"), DATE("DATE"), TIME("TIME");

	private String value;

	private PossibleDataTypes(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return this.value;
	}
}

class Column
{
	private String name;
	private PossibleDataTypes type = null;
	private Object value = null;

	protected Column(String name, PossibleDataTypes type)
	{
		this.name = name;
		this.type = type;
	}

	protected Column(String name, Object value)
	{
		this.name = name;
		this.value = value;
	}

	protected String getName()
	{
		return name;
	}

	protected PossibleDataTypes getDataType()
	{
		return type;
	}

	protected Object getValue()
	{
		return value;
	}
}
