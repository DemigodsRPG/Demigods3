package com.censoredsoftware.Demigods.Demo.Data.Deity.Titan;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.API.AbilityAPI;
import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.MiscAPI;
import com.censoredsoftware.Demigods.API.ZoneAPI;
import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class Prometheus extends Deity
{
	private static String name = "Prometheus", alliance = "Titan";
	private static ChatColor color = ChatColor.GOLD;
	private static List<Material> claimItems = new ArrayList<Material>()
	{
		{
			add(Material.DIRT);
		}
	};
	private static List<String> lore = new ArrayList<String>()
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : claimItems)
			{
				add(ChatColor.GRAY + " -> " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private static Type type = Type.DEMO;
	private static List<Ability> abilities = new ArrayList<Ability>()
	{
		{
			add(new ShootFireball());
			add(new Blaze());
		}
	};

	public Prometheus()
	{
		super(new DeityInfo(name, alliance, color, claimItems, lore, type), abilities);
	}

	public static void shootFireball(Location from, Location to, Player player)
	{
		player.getWorld().spawnEntity(from, EntityType.FIREBALL);
		for(Entity entity : player.getNearbyEntities(2, 2, 2))
		{
			if(!(entity instanceof Fireball)) continue;

			Fireball fireball = (Fireball) entity;
			to.setX(to.getX() + .5);
			to.setY(to.getY() + .5);
			to.setZ(to.getZ() + .5);
			Vector path = to.toVector().subtract(from.toVector());
			Vector victor = from.toVector().add(from.getDirection().multiply(2));
			fireball.teleport(new Location(player.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
			fireball.setDirection(path);
			fireball.setShooter(player);
		}
	}
}

class ShootFireball extends Ability
{
	private static String deity = "Prometheus", name = "Fireball", command = "fireball", permission = "demigods.titan.protmetheus";
	private static int cost = 100, delay = 5, cooldownMin = 0, cooldownMax = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/fireball" + ChatColor.WHITE + " - Shoot a fireball at the cursor's location.");
		}
	};
	private static Type type = Type.OFFENSE;

	protected ShootFireball()
	{
		super(new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!AbilityAPI.isClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();

				if(!MiscAPI.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isEnabledAbility(name) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getMeta().getBind(name))))
				{
					if(!CharacterAPI.isCooledDown(player, name, false)) return;

					fireball(player);
				}
			}
		});
	}

	// The actual ability command
	public static void fireball(Player player)
	{
		// Define variables
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "fireball", cost, type)) return;
		CharacterAPI.setCoolDown(player, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!AbilityAPI.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			Prometheus.shootFireball(player.getEyeLocation(), target.getLocation(), player);
		}
	}
}

class Blaze extends Ability
{
	private static String deity = "Prometheus", name = "Blaze", command = "blaze", permission = "demigods.titan.protmetheus";
	private static int cost = 400, delay = 15, cooldownMin = 0, cooldownMax = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/blaze" + ChatColor.WHITE + " - Ignite the ground at the target location.");
		}
	};
	private static Type type = Type.OFFENSE;

	protected Blaze()
	{
		super(new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!AbilityAPI.isClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();

				if(!MiscAPI.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isEnabledAbility(name) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getMeta().getBind(name))))
				{
					if(!CharacterAPI.isCooledDown(player, name, false)) return;

					blaze(player);
				}
			}
		});
	}

	// The actual ability command
	public static void blaze(Player player)
	{
		// Define variables
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		LivingEntity target = AbilityAPI.autoTarget(player);
		int power = character.getMeta().getLevel("OFFENSE");
		int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
		if(diameter > 12) diameter = 12;

		if(!AbilityAPI.doAbilityPreProcess(player, target, name, cost, type)) return;
		CharacterAPI.setCoolDown(player, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!AbilityAPI.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			for(int X = -diameter / 2; X <= diameter / 2; X++)
			{
				for(int Y = -diameter / 2; Y <= diameter / 2; Y++)
				{
					for(int Z = -diameter / 2; Z <= diameter / 2; Z++)
					{
						Block block = target.getWorld().getBlockAt(target.getLocation().getBlockX() + X, target.getLocation().getBlockY() + Y, target.getLocation().getBlockZ() + Z);
						if((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !ZoneAPI.zoneNoBuild(player, block.getLocation()))) block.setType(Material.FIRE);
					}
				}
			}
		}
	}
}
