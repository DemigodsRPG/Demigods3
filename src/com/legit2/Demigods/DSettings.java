package com.legit2.Demigods;

public class DSettings
{
	/*
	 * Database Options 
	 */
	// Load from config.yml
	public static boolean mysql = DConfig.getSettingBoolean("database.mysql.use");
	public static String db_name = DConfig.getSettingString("database.mysql.db_name");
	public static String username = DConfig.getSettingString("database.mysql.username");
	public static String password = DConfig.getSettingString("database.mysql.password");
	public static String host = DConfig.getSettingString("database.mysql.host");
	public static String port = DConfig.getSettingString("database.mysql.port");
	
	// Setup locally
	public static String playerdata_table = "dg_playerdata";
	public static String player_table = "dg_players";
}