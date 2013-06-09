package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Battle.Battle;

public class BattleUtility
{
	public static boolean existsNear(Location location)
	{
		return getNear(location) != null;
	}

	public static Battle getNear(Location location)
	{
		for(Battle battle : Battle.getAll())
		{
			if(battle.getStartLocation().distance(location) <= battle.getRange()) return battle;
		}
		return null;
	}
}
