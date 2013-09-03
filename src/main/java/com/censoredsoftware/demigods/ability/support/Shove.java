package com.censoredsoftware.demigods.ability.support;

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
import org.bukkit.util.Vector;

import java.util.List;

public class Shove implements Ability
{
	private final static String name = "Shove", command = "shove";
	private final static int cost = 170, delay = 15, repeat = 0;
	private final static Skill.Type type = Skill.Type.SUPPORT;
	private final static List<String> details = Lists.newArrayList("Shove your target away from you.");
	private String deity, permission;

	public Shove(String deity, String permission)
	{
		this.deity = deity;
		this.permission = permission;
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
		int ascensions = character.getMeta().getAscensions();
		double multiply = 0.1753 * Math.pow(ascensions, 0.322917);
		LivingEntity target = Util.autoTarget(player);

		if(!Util.doAbilityPreProcess(player, target, cost, type)) return;
		DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!Util.doTargeting(player, target.getLocation(), true)) return;

		Vector vector = player.getLocation().toVector();
		Vector victor = target.getLocation().toVector().subtract(vector);
		victor.multiply(multiply);
		target.setVelocity(victor);
		Util.dealDamage(player, target, 0, EntityDamageEvent.DamageCause.FALL);
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
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Demigods.MiscUtil.isDisabledWorld(interactEvent.getPlayer().getWorld())) return;

				if(!Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(character, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					shove(player);
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
