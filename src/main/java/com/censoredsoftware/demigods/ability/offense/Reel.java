package com.censoredsoftware.demigods.ability.offense;

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

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;

public class Reel extends Ability
{
	public static Reel ability;
	private final static String name = "Reel", command = "reel";
	private final static int cost = 120, delay = 1100, repeat = 0;

	private final static Devotion.Type type = Devotion.Type.OFFENSE;
	private final static Material weapon = Material.FISHING_ROD;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Use a fishing rod for a stronger attack.");
		}
	};

	public Reel(final String deity, String permission)
	{
		super(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Demigods.isDisabledWorld(interactEvent.getPlayer().getWorld())) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Ability.Util.isLeftClick(interactEvent)) return;

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isBound(name) && player.getItemInHand().getType() == weapon)
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.reel(player);
				}
			}
		}, null, deity, name, command, permission, cost, delay, repeat, details, type, weapon);
		ability = this;
	}

	public static class Util
	{
		public static void reel(Player player)
		{
			// Set variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
			LivingEntity target = Ability.Util.autoTarget(player);

			if(!Ability.Util.doAbilityPreProcess(player, target, name, cost, ability)) return;
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