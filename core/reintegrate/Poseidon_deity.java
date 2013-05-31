package com.censoredsoftware.Demigods.Demo.Data.Deity.God;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class Poseidon_deity extends Deity implements Listener
{
	// Create required universal deity variables
	private static final String DEITYNAME = "Poseidon";
	private static final String DEITYALLIANCE = "God";
	private static final ChatColor DEITYCOLOR = ChatColor.AQUA;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/reel" Command:
	private static final String REEL_NAME = "Reel"; // Sets the name of this command
	private static final int REEL_COST = 120; // Cost to run command in "favor"
	private static final int REEL_DELAY = 1100; // In milliseconds

	// "/drown" Command:
	private static final String DROWN_NAME = "Drown"; // Sets the name of this command
	private static final int DROWN_COST = 240; // Cost to run command in "favor"
	private static final int DROWN_DELAY = 10000; // In milliseconds

	@Override
	public List<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.WATER_BUCKET);
		// claimItems.add(Material.WATER_LILY);
		claimItems.add(Material.DIRT);

		return claimItems;
	}

	@Override
	public List<String> getInfo(Player player)
	{
		ArrayList<String> toReturn = new ArrayList<String>();

		if(MiscAPI.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/reel" + ChatColor.WHITE + " - Use a fishing rod for a stronger attack.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/drown" + ChatColor.WHITE + " - Drown your enemies in sufficating water.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "Crouch while in water to swim like Poseidon.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " You are a follower of " + DEITYNAME + "!");

			return toReturn;
		}
		else
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/reel" + ChatColor.WHITE + " - Use a fishing rod for a stronger attack.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/drown" + ChatColor.WHITE + " - Drown your enemies in sufficating water.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "Crouch while in water to swim like Poseidon.");
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
			if(damageEvent.getCause() == DamageCause.DROWNING)
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

		if(character.getMeta().isEnabledAbility(REEL_NAME) && (player.getItemInHand().getType() == Material.FISHING_ROD))
		{
			if(!CharacterAPI.isCooledDown(player, REEL_NAME, false)) return;

			reel(player);
		}

		if(character.getMeta().isEnabledAbility(DROWN_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBindings().getBind(DROWN_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, DROWN_NAME, false)) return;

			drown(player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

		// PHELPS SWIMMING
		if(player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER))
		{
			Vector direction = player.getLocation().getDirection().normalize().multiply(1.3D);
			Vector victor = new Vector(direction.getX(), direction.getY(), direction.getZ());
			if(player.isSneaking())
			{
				player.setVelocity(victor);
			}
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/reel"
	 */
	public static void reelCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(character.getMeta().isEnabledAbility(REEL_NAME))
		{
			character.getMeta().toggleAbility(REEL_NAME, false);
			player.sendMessage(ChatColor.YELLOW + REEL_NAME + " is no longer active.");
		}
		else
		{
			character.getMeta().toggleAbility(REEL_NAME, true);
			player.sendMessage(ChatColor.YELLOW + REEL_NAME + " is now active.");
		}
	}

	// The actual ability command
	public static void reel(Player player)
	{
		// Set variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		int damage = (int) Math.ceil(0.37286 * Math.pow(character.getPower(AbilityType.OFFENSE), 0.371238));
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "reel", REEL_COST, AbilityType.OFFENSE)) return;
		character.subtractFavor(REEL_COST);
		CharacterAPI.setCoolDown(player, REEL_NAME, System.currentTimeMillis() + REEL_DELAY);

		if(!AbilityAPI.targeting(player, target)) return;

		MiscAPI.customDamage(player, target, damage, DamageCause.CUSTOM);

		if(target.getLocation().getBlock().getType() == Material.AIR)
		{
			target.getLocation().getBlock().setType(Material.WATER);
			target.getLocation().getBlock().setData((byte) 0x8);
		}
	}

	/*
	 * Command: "/drown"
	 */
	public static void drownCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.getBindings().setBound(DROWN_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.getMeta().isEnabledAbility(DROWN_NAME))
			{
				character.getMeta().toggleAbility(DROWN_NAME, false);
				player.sendMessage(ChatColor.YELLOW + DROWN_NAME + " is no longer active.");
			}
			else
			{
				character.getMeta().toggleAbility(DROWN_NAME, true);
				player.sendMessage(ChatColor.YELLOW + DROWN_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void drown(Player player)
	{
		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		int power = character.getPower(AbilityType.OFFENSE);
		int radius = (int) Math.ceil(1.6955424 * Math.pow(power, 0.129349));
		int duration = (int) Math.ceil(2.80488 * Math.pow(power, 0.2689)); // seconds
		LivingEntity target = AbilityAPI.autoTarget(player);
		if(target == null) return; // Null check
		Location toHit = AbilityAPI.aimLocation(character, target.getLocation());

		if(!AbilityAPI.doAbilityPreProcess(player, target, "drown", DROWN_COST, AbilityType.OFFENSE)) return;
		character.subtractFavor(DROWN_COST);

		// Set the ability's delay
		CharacterAPI.setCoolDown(player, DROWN_NAME, System.currentTimeMillis() + DROWN_DELAY);

		final ArrayList<Block> toReset = new ArrayList<Block>();
		for(int x = -radius; x <= radius; x++)
		{
			for(int y = -radius; y <= radius; y++)
			{
				for(int z = -radius; z <= radius; z++)
				{
					Block block = toHit.getWorld().getBlockAt(toHit.getBlockX() + x, toHit.getBlockY() + y, toHit.getBlockZ() + z);
					if(block.getLocation().distance(toHit) <= radius)
					{
						if(block.getType() == Material.AIR)
						{
							block.setType(Material.WATER);
							block.setData((byte) (0x8));
							toReset.add(block);
						}
					}
				}
			}
		}

		if(!AbilityAPI.isHit(target, toHit))
		{
			player.sendMessage(ChatColor.RED + "Missed...");
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				for(Block block : toReset)
				{
					if((block.getType() == Material.WATER) || (block.getType() == Material.STATIONARY_WATER)) block.setType(Material.AIR);
				}
			}
		}, duration);
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
		COMMANDS.add("reel");
		COMMANDS.add("drown");

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
