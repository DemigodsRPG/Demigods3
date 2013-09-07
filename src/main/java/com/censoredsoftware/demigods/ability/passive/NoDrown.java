package com.censoredsoftware.demigods.ability.passive;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.player.Character;
import com.censoredsoftware.demigods.player.Skill;
import com.google.common.collect.Lists;

public class NoDrown implements Ability
{
	private final static String name = "No Drown Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 1;
	private final static Skill.Type type = Skill.Type.PASSIVE;
	private final static List<String> details = Lists.newArrayList("You cannot drown.");
	private String deity, permission;

	public NoDrown(final String deity, String permission)
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
	public Skill.Type getType()
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
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(Demigods.MiscUtil.isDisabledWorld(damageEvent.getEntity().getWorld())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives falling damage, cancel it
					if(damageEvent.getCause() == EntityDamageEvent.DamageCause.DROWNING) damageEvent.setCancelled(true);
				}
			}
		};
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Character character : com.censoredsoftware.demigods.player.Character.Util.getOnlineCharactersWithDeity(deity))
				{
					if(Demigods.MiscUtil.isDisabledWorld(character.getOfflinePlayer().getPlayer().getWorld())) continue;
					character.getOfflinePlayer().getPlayer().setRemainingAir(0);
				}
			}
		};
	}
}
