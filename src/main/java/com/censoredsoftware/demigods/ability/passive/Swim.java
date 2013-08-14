package com.censoredsoftware.demigods.ability.passive;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.deity.Deity;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Swim implements Ability
{
	private final static String name = "Swim", command = null;
	private final static int cost = 0, delay = 0, repeat = 20;
	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = Lists.newArrayList("Crouch while in water to swim very fast.");
	private String deity, permission;

	public Swim(final String deity, String permission)
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
			@EventHandler(priority = EventPriority.HIGHEST)
			private void onPlayerMoveEvent(PlayerMoveEvent event)
			{
				if(Demigods.isDisabledWorld(event.getPlayer().getWorld())) return;

				Player player = event.getPlayer();
				boolean inWater = player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER);

				if(player.isSneaking() && inWater && DataManager.hasKeyTemp(player.getName(), "is_swimming"))
				{
					Vector direction = player.getLocation().getDirection().normalize().multiply(1.3D);
					Vector victor = new Vector(direction.getX(), direction.getY(), direction.getZ());
					player.setVelocity(victor);
					return;
				}
				else if(!player.isSneaking() && !inWater)
				{
					DataManager.removeTemp(player.getName(), "is_swimming");
					return;
				}
				else if(player.isSneaking() && inWater && Deity.Util.canUseDeitySilent(player, deity)) // TODO There has to be a quicker way to do this.
				{
					DataManager.saveTemp(player.getName(), "is_swimming", true);
					return;
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
