package com.censoredsoftware.demigods.ability.passive;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.Skill;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AlwaysInvisible implements Ability
{
	private final static String name = "Invisible", command = null;
	private final static int cost = 0, delay = 0, repeat = 20;
	private final static Skill.Type type = Skill.Type.PASSIVE;
	private final static List<String> details = Lists.newArrayList("Always invisible.");
	private String deity, permission;

	public AlwaysInvisible(String deity, String permission)
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
				for(DCharacter character : DCharacter.Util.getOnlineCharactersWithDeity(deity))
				{
					if(Demigods.MiscUtil.isDisabledWorld(character.getOfflinePlayer().getPlayer().getWorld())) continue;
					Player player = character.getOfflinePlayer().getPlayer();
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 1));
				}
			}
		};
	}
}
