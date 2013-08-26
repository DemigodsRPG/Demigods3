package com.censoredsoftware.demigods.ability.passive;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NoFire implements Ability
{
	private final static String name = "No Fire Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;
	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = Lists.newArrayList("Take no damage from fire.");
	private String deity, permission;

	public NoFire(String deity, String permission)
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
	public Devotion.Type getType()
	{
		return type;
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
			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(Demigods.isDisabledWorld(damageEvent.getEntity().getWorld())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives falling damage, cancel it
					if(damageEvent.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || damageEvent.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) damageEvent.setCancelled(true);
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