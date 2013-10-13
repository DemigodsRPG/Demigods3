package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NoDrown implements Ability
{
	private final static String name = "No Drown Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 1;
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
		return null;
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(DCharacter character : DCharacter.Util.getOnlineCharactersWithAbility(name))
				{
					if(Zones.inNoDemigodsZone(character.getOfflinePlayer().getPlayer().getLocation())) continue;
					Player player = character.getOfflinePlayer().getPlayer();
					potionEffect(player);
				}
			}

			private void potionEffect(LivingEntity entity)
			{
				entity.removePotionEffect(PotionEffectType.WATER_BREATHING);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 1));
			}
		};
	}
}
