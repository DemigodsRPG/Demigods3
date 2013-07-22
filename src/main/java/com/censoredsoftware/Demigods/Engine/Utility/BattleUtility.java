package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Object.Battle.BattleParticipant;
import com.censoredsoftware.Demigods.Engine.Object.Mob.TameableWrapper;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;

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

	public static boolean isInBattle(BattleParticipant participant)
	{
		for(Battle battle : Battle.getAll())
		{
			if(battle.getMeta().getParticipants().contains(participant)) return true;
		}
		return false;
	}

	public static Battle getBattle(BattleParticipant participant)
	{
		for(Battle battle : Battle.getAll())
		{
			if(battle.getMeta().getParticipants().contains(participant)) return battle;
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

	public static Location randomRespawnPoint(Battle battle)
	{
		List<Location> respawnPoints = getSafeRespawnPoints(battle);
		if(respawnPoints.size() == 0) return battle.getStartLocation();

		Location target = respawnPoints.get(MiscUtility.generateIntRange(0, respawnPoints.size() - 1));

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

	public static boolean isSafeLocation(Location reference, Location checking)
	{
		if(reference.getBlock().getType().isSolid() || reference.getBlock().getType().equals(Material.LAVA)) return false;
		double referenceY = reference.getY();
		double checkingY = checking.getY();
		if(Math.abs(referenceY - checkingY) > 5) return false;
		return true;
	}

	public static List<Location> getSafeRespawnPoints(final Battle battle)
	{
		return new ArrayList<Location>()
		{
			{
				for(Location location : MiscUtility.getCirclePoints(battle.getStartLocation(), battle.getRange() - 1.5, 20))
				{
					if(isSafeLocation(battle.getStartLocation(), location)) add(location);
				}
			}
		};
	}

	public static boolean canParticipate(Entity entity)
	{
		if(!(entity instanceof Player) && !(entity instanceof Tameable)) return false;
		if(entity instanceof Player && PlayerWrapper.getPlayer((Player) entity).getCurrent() == null) return false;
		if(entity instanceof Tameable && TameableWrapper.getTameable((LivingEntity) entity) == null) return false;
		return true;
	}

	public static BattleParticipant defineParticipant(Entity entity)
	{
		if(entity instanceof Player) return PlayerWrapper.getPlayer((Player) entity).getCurrent();
		return TameableWrapper.getTameable((LivingEntity) entity);
	}

	public static void teleportIfNeeded(BattleParticipant participant, Battle battle)
	{
		if(participant.getRelatedCharacter().getOfflinePlayer().isOnline() && !BattleUtility.existsInRadius(participant.getRelatedCharacter().getOfflinePlayer().getPlayer().getLocation())) participant.getRelatedCharacter().getOfflinePlayer().getPlayer().teleport(BattleUtility.randomRespawnPoint(battle));
	}

	public static boolean battleDeath(BattleParticipant damager, BattleParticipant damagee, Battle battle)
	{
		if(damager instanceof PlayerCharacter) ((PlayerCharacter) damager).addKill();
		if(damager.getRelatedCharacter().getOfflinePlayer().isOnline()) damager.getRelatedCharacter().getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "+1 Kill.");
		battle.getMeta().addKill(damager);
		return battleDeath(damagee, battle);
	}

	public static boolean battleDeath(BattleParticipant damagee, Battle battle)
	{
		damagee.getEntity().setHealth(damagee.getEntity().getMaxHealth());
		damagee.getEntity().teleport(BattleUtility.randomRespawnPoint(battle));
		if(damagee instanceof PlayerCharacter) ((PlayerCharacter) damagee).addDeath();
		if(damagee.getRelatedCharacter().getOfflinePlayer().isOnline()) damagee.getRelatedCharacter().getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "+1 Death.");
		battle.getMeta().addDeath(damagee);
		return true;
	}
}
