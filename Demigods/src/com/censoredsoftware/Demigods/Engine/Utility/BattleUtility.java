package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Demigods;

public class BattleUtility
{
	public static boolean existsNear(Location location)
	{
		return getNear(location) != null;
	}

	public static Battle getNear(Location location)
	{
		int default_range = Demigods.config.getSettingInt("battle.min_range");
		int range = 0;
		for(Battle battle : Battle.getAll())
		{
			if(battle.getRange() > default_range) range = battle.getRange();
			if(battle.getStartLocation().distance(location) <= range) return battle;
		}
		return null;
	}
}
