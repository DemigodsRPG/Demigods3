package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Effect;
import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;

public class BattleUtility
{
	public static boolean existsInRadius(Location location)
	{
		return getInRadius(location) != null;
	}

	public static Battle getInRadius(Location location)
	{
		for(Battle battle : Battle.getAll())
		{
			if(battle.getStartLocation().distance(location) <= battle.getRange()) return battle;
		}
		return null;
	}

	public static boolean existsNear(Location location)
	{
		return getInRadius(location) != null;
	}

	public static Battle getNear(Location location)
	{
		for(Battle battle : Battle.getAll())
		{
			double distance = battle.getStartLocation().distance(location);
			if(distance > battle.getRange() && distance <= Demigods.config.getSettingInt("battles.merge_range")) return battle;
		}
		return null;
	}

	public static void battleBorder(Battle battle)
	{
		if(!SpigotUtility.runningSpigot()) return;
		SpigotUtility.drawCircle(battle.getStartLocation(), Effect.WITCH_MAGIC, 16, 120);
	}

}
