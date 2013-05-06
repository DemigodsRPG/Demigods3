package com.censoredsoftware.Demigods.Demo.Data.Deity.God;

import java.util.ArrayList;
import java.util.List;

import com.censoredsoftware.Demigods.Engine.Demigods;
import org.bukkit.Bukkit;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class Zeus_deity extends Deity implements Listener
{
	// Create required universal deity variables
	private static final String DEITYNAME = "Zeus";
	private static final String DEITYALLIANCE = "God";
	private static final ChatColor DEITYCOLOR = ChatColor.YELLOW;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/shove" Command:
	private static final String SHOVE_NAME = "Shove"; // Sets the name of this command
	private static final int SHOVE_COST = 170; // Cost to run command in "favor"
	private static final int SHOVE_DELAY = 1500; // In milliseconds

	// "/lightning" Command:
	private static final String LIGHTNING_NAME = "Lightning"; // Sets the name of this command
	private static final int LIGHTNING_COST = 140; // Cost to run command in "favor"
	private static final int LIGHTNING_DELAY = 1000; // In milliseconds

	// "/storm" Command:
	@SuppressWarnings("unused")
	private static String ULTIMATE_NAME = "Storm";
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	@Override
	public List<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.IRON_INGOT);
		// claimItems.add(Material.FEATHER);
		claimItems.add(Material.DIRT);

		return claimItems;
	}

	@Override
	public ArrayList<String> getInfo(Player player)
	{
		ArrayList<String> toReturn = new ArrayList<String>();

		if(MiscAPI.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/shove" + ChatColor.WHITE + " - Shove your target away from you.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/lightning" + ChatColor.WHITE + " - Strike lightning upon your enemies.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "Take no damage from falling.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/storm" + ChatColor.WHITE + " - Throw all of your enemies into the sky as lightning fills the heavens.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " You are a follower of " + DEITYNAME + "!");
			toReturn.add(" ");

			return toReturn;
		}
		else
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/shove" + ChatColor.WHITE + " - Shove your target away from you.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/lightning" + ChatColor.WHITE + " - Strike lightning upon your enemies.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "Take no damage from falling.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/storm" + ChatColor.WHITE + " - Throw all of your enemies into the sky as lightning fills the heavens.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : getClaimItems())
			{
				toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + item.name());
			}
			toReturn.add(" ");

			return toReturn;
		}
	}

	// This sets the particular passive ability for the Zeus_deity deity.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageEvent damageEvent)
	{
		if(damageEvent.getEntity() instanceof Player)
		{
			Player player = (Player) damageEvent.getEntity();
			if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

			// If the player receives falling damage, cancel it
			if(damageEvent.getCause() == DamageCause.FALL)
			{
				damageEvent.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!AbilityAPI.isClick(interactEvent)) return;

		if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

		if(character.getMeta().isEnabledAbility(SHOVE_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBindings().getBind(SHOVE_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, SHOVE_NAME, false)) return;

			shove(player);
		}

		if(character.getMeta().isEnabledAbility(LIGHTNING_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBindings().getBind(LIGHTNING_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, LIGHTNING_NAME, false)) return;

			lightning(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/shove"
	 */
	public static void shoveCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.getBindings().setBound(SHOVE_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.getMeta().isEnabledAbility(SHOVE_NAME))
			{
				character.getMeta().toggleAbility(SHOVE_NAME, false);
				player.sendMessage(ChatColor.YELLOW + SHOVE_NAME + " is no longer active.");
			}
			else
			{
				character.getMeta().toggleAbility(SHOVE_NAME, true);
				player.sendMessage(ChatColor.YELLOW + SHOVE_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		int devotion = character.getDevotion();
		double multiply = 0.1753 * Math.pow(devotion, 0.322917);
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "shove", SHOVE_COST, AbilityType.PASSIVE)) return;
		CharacterAPI.setCoolDown(player, SHOVE_NAME, System.currentTimeMillis() + SHOVE_DELAY);
		character.subtractFavor(SHOVE_COST);

		if(!AbilityAPI.targeting(player, target)) return;

		Vector vector = player.getLocation().toVector();
		Vector victor = target.getLocation().toVector().subtract(vector);
		victor.multiply(multiply);
		target.setVelocity(victor);
	}

	/*
	 * Command: "/lightning"
	 */
	public static void lightningCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.getBindings().setBound(LIGHTNING_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.getMeta().isEnabledAbility(LIGHTNING_NAME))
			{
				character.getMeta().toggleAbility(LIGHTNING_NAME, false);
				player.sendMessage(ChatColor.YELLOW + LIGHTNING_NAME + " is no longer active.");
			}
			else
			{
				character.getMeta().toggleAbility(LIGHTNING_NAME, true);
				player.sendMessage(ChatColor.YELLOW + LIGHTNING_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void lightning(Player player)
	{
		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "lightning", LIGHTNING_COST, AbilityType.OFFENSE)) return;
		CharacterAPI.setCoolDown(player, LIGHTNING_NAME, System.currentTimeMillis() + LIGHTNING_DELAY);
		character.subtractFavor(LIGHTNING_COST);

		strikeLightning(player, target);
	}

	/*
	 * Command: "/storm"
	 */
	public static void stormCommand(Player player, String[] args)
	{
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME + ".ultimate")) return;

		// Set variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		// Check the player for DEITYNAME
		if(!character.isDeity(DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(CharacterAPI.isCooledDown(player, ULTIMATE_NAME, false))
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + (((CharacterAPI.getCoolDown(player, ULTIMATE_NAME) / 1000) - (System.currentTimeMillis() / 1000))) / 60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((CharacterAPI.getCoolDown(player, ULTIMATE_NAME)) / 1000) - (System.currentTimeMillis() / 1000)) % 60) + " seconds.");
			return;
		}

		if(!AbilityAPI.doAbilityPreProcess(player, "storm", ULTIMATE_COST, AbilityType.OFFENSE)) return;

		// Perform ultimate if there is enough favor
		int count = storm(player);
		if(count == 0)
		{
			player.sendMessage(ChatColor.YELLOW + "Zeus unable to strike any targets.");
			return;
		}

		player.sendMessage(ChatColor.YELLOW + "Zeus has struck " + count + " targets!");

		// Set favor and cooldown
		character.subtractFavor(ULTIMATE_COST);
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		CharacterAPI.setCoolDown(player, ULTIMATE_NAME, System.currentTimeMillis() + cooldownMultiplier * 1000);
	}

	// The actual ability command
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
						if(strikeLightning(player, otherPlayer)) count++;
						strikeLightning(player, otherPlayer);
						strikeLightning(player, otherPlayer);
					}
				}
				else if(entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					if(strikeLightning(player, livingEntity)) count++;
					strikeLightning(player, livingEntity);
					strikeLightning(player, livingEntity);
				}
			}
			catch(Exception ignored)
			{}
		}

		return count;
	}

	private static boolean strikeLightning(Player player, LivingEntity target)
	{
		// Set variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

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
				if(livingEntity.getLocation().distance(toHit) < 1.5) MiscAPI.customDamage(player, livingEntity, character.getAscensions() * 2, DamageCause.LIGHTNING);
			}
		}

		if(!AbilityAPI.isHit(target, toHit))
		{
			player.sendMessage(ChatColor.RED + "Missed...");
		}

		return true;
	}

	@Override
	public String loadDeity()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Demigods.plugin);
		return DEITYNAME + " loaded.";
	}

	@Override
	public List<String> getCommands()
	{
		ArrayList<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("shove");
		COMMANDS.add("lightning");
		COMMANDS.add("storm");

		return COMMANDS;
	}

	@Override
	public String getName()
	{
		return DEITYNAME;
	}

	@Override
	public String getAlliance()
	{
		return DEITYALLIANCE;
	}

	@Override
	public ChatColor getColor()
	{
		return DEITYCOLOR;
	}
}
