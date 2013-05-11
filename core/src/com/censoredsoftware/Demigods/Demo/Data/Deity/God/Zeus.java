package com.censoredsoftware.Demigods.Demo.Data.Deity.God;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.API.*;
import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class Zeus extends Deity
{
	private static String name = "Zeus", alliance = "God";
	private static ChatColor color = ChatColor.YELLOW;
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
			add(new NoFall());
			add(new Shove());
			add(new Lightning());
			add(new Storm());
		}
	};

	public Zeus()
	{
		super(new DeityInfo(name, alliance, color, claimItems, lore, type), abilities);
	}

	protected static boolean strikeLightning(Player player, LivingEntity target)
	{
		// Set variables
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();

		if(!player.getWorld().equals(target.getWorld())) return false;
		if(!ZoneAPI.canTarget(target)) return false;
		Location toHit = AbilityAPI.aimLocation(character, target.getLocation());

		player.getWorld().strikeLightningEffect(toHit);

		for(Entity entity : toHit.getBlock().getChunk().getEntities())
		{
			if(entity instanceof LivingEntity)
			{
				if(!ZoneAPI.canTarget(entity)) continue;
				LivingEntity livingEntity = (LivingEntity) entity;
				if(livingEntity.getLocation().distance(toHit) < 1.5) MiscAPI.customDamage(player, livingEntity, character.getMeta().getAscensions() * 2, EntityDamageEvent.DamageCause.LIGHTNING);
			}
		}

		if(!AbilityAPI.isHit(target, toHit))
		{
			player.sendMessage(ChatColor.RED + "Missed...");
		}

		return true;
	}
}

class Shove extends Ability
{
	private static String deity = "Zeus", name = "Shove", command = "shove", permission = "demigods.god.zeus";
	private static int cost = 170, delay = 1500, cooldownMin = 0, cooldownMax = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/shove" + ChatColor.WHITE + " - Shove your target away from you.");
		}
	};
	private static Type type = Type.DEFENSE;

	protected Shove()
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

					shove(player);
				}
			}
		});
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		int devotion = character.getMeta().getDevotion();
		double multiply = 0.1753 * Math.pow(devotion, 0.322917);
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "shove", cost, type)) return;
		CharacterAPI.setCoolDown(player, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!AbilityAPI.targeting(player, target)) return;

		Vector vector = player.getLocation().toVector();
		Vector victor = target.getLocation().toVector().subtract(vector);
		victor.multiply(multiply);
		target.setVelocity(victor);
	}
}

class Lightning extends Ability
{
	private static String deity = "Zeus", name = "Lighting", command = "lightning", permission = "demigods.god.zeus";
	private static int cost = 140, delay = 1000, cooldownMin = 0, cooldownMax = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/lightning" + ChatColor.WHITE + " - Strike lightning upon your enemies.");
		}
	};
	private static Ability.Type type = Ability.Type.OFFENSE;

	protected Lightning()
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

					lightning(player);
				}
			}
		});
	}

	protected static void lightning(Player player)
	{
		// Define variables
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "lightning", cost, type)) return;
		CharacterAPI.setCoolDown(player, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		Zeus.strikeLightning(player, target);
	}
}

class Storm extends Ability
{
	private static String deity = "Zeus", name = "Storm", command = "storm", permission = "demigods.god.zeus.ultimate";
	private static int cost = 3700, delay = 1500, cooldownMin = 60, cooldownMax = 600;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/storm" + ChatColor.WHITE + " - Throw all of your enemies into the sky as lightning fills the heavens.");
		}
	};
	private static Type type = Type.ULTIMATE;

	protected Storm()
	{
		super(new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
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

					storm(player);

					int cooldownMultiplier = (int) (cooldownMax - ((cooldownMax - cooldownMin) * ((double) character.getMeta().getAscensions() / 100)));
					CharacterAPI.setCoolDown(player, name, System.currentTimeMillis() + cooldownMultiplier * 1000);
				}
			}
		});
	}

	public static int storm(Player player)
	{
		// Define variables
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		Vector playerLocation = player.getLocation().toVector();

		for(Entity anEntity : player.getWorld().getEntities())
			if(anEntity.getLocation().toVector().isInSphere(playerLocation, 50.0)) entityList.add(anEntity);

		int count = 0;
		for(Entity entity : entityList)
		{
			try
			{
				if(entity instanceof Player)
				{
					Player otherPlayer = (Player) entity;
					if(!PlayerAPI.areAllied(player, otherPlayer) && !otherPlayer.equals(player))
					{
						if(Zeus.strikeLightning(player, otherPlayer)) count++;
						Zeus.strikeLightning(player, otherPlayer);
						Zeus.strikeLightning(player, otherPlayer);
					}
				}
				else if(entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					if(Zeus.strikeLightning(player, livingEntity)) count++;
					Zeus.strikeLightning(player, livingEntity);
					Zeus.strikeLightning(player, livingEntity);
				}
			}
			catch(Exception ignored)
			{}
		}

		return count;
	}
}

class NoFall extends Ability
{
	private static String deity = "Zeus", name = "No Fall Damage", command = null, permission = "demigods.god.zeus";
	private static int cost = 0, delay = 0, cooldownMin = 0, cooldownMax = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "Take no damage from falling.");
		}
	};
	private static Ability.Type type = Type.PASSIVE;

	protected NoFall()
	{
		super(new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!MiscAPI.canUseDeitySilent(player, name)) return;

					// If the player receives falling damage, cancel it
					if(damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) damageEvent.setCancelled(true);
				}
			}
		});
	}
}
