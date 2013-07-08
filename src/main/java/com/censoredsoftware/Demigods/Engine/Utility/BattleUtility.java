package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

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
		drawCircle(battle.getStartLocation(), battle.getRange(), 60);
	}

	public static void drawCircle(Location center, double radius, int points)
	{
		if(!SpigotUtility.runningSpigot()) return;
		for(Location point : getCirclePoints(center, radius, points))
		{
			SpigotUtility.playParticle(point, Effect.FLAME, 0, 1, 0, 10F, 30, 50);
		}
	}

	private static Set<Location> getCirclePoints(Location center, final double radius, final int points)
	{
		final World world = center.getWorld();
		final double X = center.getX();
		final double Y = center.getY();
		final double Z = center.getZ();
		return new HashSet<Location>()
		{
			{
				for(int i = 0; i < points; i++)
				{
					double x = X + radius * Math.cos((2 * Math.PI * i) / points);
					double z = Z + radius * Math.sin((2 * Math.PI * i) / points);
					add(new Location(world, x, Y, z));
				}
			}
		};
	}
}
