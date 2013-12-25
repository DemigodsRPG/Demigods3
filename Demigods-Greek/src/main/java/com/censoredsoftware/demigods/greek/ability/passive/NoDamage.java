package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NoDamage implements Ability
{
	private final static String name = "No Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Take no corrupt, give no corrupt.");
	private String deity, permission;

	public NoDamage(String deity, String permission)
	{
		this.deity = deity;
		this.permission = permission;
	}

	@Override
	public String getDeity()
	{
		return deity;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getCommand()
	{
		return command;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public int getCost()
	{
		return cost;
	}

	@Override
	public int getDelay()
	{
		return delay;
	}

	@Override
	public int getRepeat()
	{
		return repeat;
	}

	@Override
	public List<String> getDetails()
	{
		return details;
	}

	@Override
	public Material getWeapon()
	{
		return null;
	}

	@Override
	public boolean hasWeapon()
	{
		return getWeapon() != null;
	}

	@Override
	public Listener getListener()
	{
		return new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives corrupt, cancel it
					damageEvent.setCancelled(true);
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityDamageByEntity(EntityDamageByEntityEvent damageEvent)
			{
				if(Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
				if(damageEvent.getDamager() instanceof Player)
				{
					Player player = (Player) damageEvent.getDamager();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives corrupt, cancel it
					damageEvent.setCancelled(true);
				}
			}
		};
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return null;
	}
}
