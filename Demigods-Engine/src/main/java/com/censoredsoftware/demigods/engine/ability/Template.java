package com.censoredsoftware.demigods.engine.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.player.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;

public class Template implements Ability
{
	private final static String name = "Test", command = "test";
	private final static int cost = 170, delay = 1500, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Test your target.");
	private String deity, permission;
	private final static Skill.Type type = Skill.Type.OFFENSE;

	public Template(String deity, String permission)
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
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation())) return;

				if(!Abilities.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.test(player);
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
		// The actual ability command
		public static void test(Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			LivingEntity target = Abilities.autoTarget(player);

			if(!Abilities.doAbilityPreProcess(player, target, cost)) return;
			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
			character.getMeta().subtractFavor(cost);

			if(!Abilities.doTargeting(player, target.getLocation(), true)) return;

			if(target instanceof Player)
			{
				Player victim = (Player) target;
				victim.sendMessage("Test!");
				player.sendMessage("Tested " + victim.getName() + "!");
			}
		}
	}
}
