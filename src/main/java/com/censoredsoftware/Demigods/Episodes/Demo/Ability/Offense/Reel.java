package com.censoredsoftware.Demigods.Episodes.Demo.Ability.Offense;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.Demigods.Engine.Object.Ability;
import com.censoredsoftware.Demigods.Engine.Object.DPlayer;
import com.censoredsoftware.Demigods.Engine.Object.Deity;

public class Reel extends Ability
{
	private final static String name = "Reel", command = "reel";
	private final static int cost = 120, delay = 1100, repeat = 0;
	private static Info info;
	private final static Material weapon = Material.FISHING_ROD;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Use a fishing rod for a stronger attack.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.OFFENSE;

	public Reel(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type, weapon), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				// Set variables
				Player player = interactEvent.getPlayer();
				DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Ability.Util.isLeftClick(interactEvent)) return;

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isBound(name) && player.getItemInHand().getType() == weapon)
				{
					if(!DPlayer.Character.Util.isCooledDown(character, name, false)) return;

					Util.reel(player);
				}
			}
		}, null);
	}

	public static class Util
	{
		public static void reel(Player player)
		{
			// Set variables
			DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();
			int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
			LivingEntity target = Ability.Util.autoTarget(player);

			if(!Ability.Util.doAbilityPreProcess(player, target, name, cost, info)) return;
			character.getMeta().subtractFavor(cost);
			DPlayer.Character.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);

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
