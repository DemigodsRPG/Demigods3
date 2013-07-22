package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
		return getNear(location) != null;
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
		SpigotUtility.drawCircle(battle.getStartLocation(), Effect.MOBSPAWNER_FLAMES, battle.getRange(), 120);
	}

	public static Location randomBorderLocation(Battle battle)
	{
		Location target = MiscUtility.getCirclePoints(battle.getStartLocation(), battle.getRange() - 1.5, 20).get(MiscUtility.generateIntRange(0, 19));

		Vector direction = target.toVector().subtract(battle.getStartLocation().toVector()).normalize();
		double X = direction.getX();
		double Y = direction.getY();
		double Z = direction.getZ();

		// Now change the angle
		Location changed = target.clone();
		changed.setYaw(180 - MiscUtility.toDegree(Math.atan2(X, Y)));
		changed.setPitch(90 - MiscUtility.toDegree(Math.acos(Z)));
		return changed;
	}
}
