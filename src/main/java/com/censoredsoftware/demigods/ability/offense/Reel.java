package com.censoredsoftware.demigods.ability.offense;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Skill;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Reel implements Ability
{
	private final static String name = "Reel", command = "reel";
	private final static int cost = 120, delay = 1100, repeat = 0;
	private final static Skill.Type type = Skill.Type.OFFENSE;
	private final static Material weapon = Material.FISHING_ROD;
	private final static List<String> details = Lists.newArrayList("Use a fishing rod for a stronger attack.");
	private String deity, permission;

	public Reel(String deity, String permission)
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
		return weapon;
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
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Demigods.MiscUtil.isDisabledWorld(interactEvent.getPlayer().getWorld())) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Ability.Util.isLeftClick(interactEvent)) return;

				if(!Deity.Util.canUseDeitySilent(character, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.reel(player);
				}
			}
		};
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return null;
	}

	public static class Util
	{
		public static void reel(Player player)
		{
			// Set variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
			LivingEntity target = Ability.Util.autoTarget(player);

			if(!Ability.Util.doAbilityPreProcess(player, target, cost, type)) return;
			character.getMeta().subtractFavor(cost);
			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);

			if(!Ability.Util.doTargeting(player, target.getLocation(), true)) return;

			Ability.Util.dealDamage(player, target, damage, EntityDamageEvent.DamageCause.CUSTOM);

			if(target.getLocation().getBlock().getType() == Material.AIR)
			{
				target.getLocation().getBlock().setType(Material.WATER);
				target.getLocation().getBlock().setData((byte) 0x8);
			}
		}
	}
}
