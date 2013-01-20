package com.legit2.Demigods.Deities.Gods;

import java.util.ArrayList;

import org.bukkit.ChatColor;
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

import com.google.common.base.Joiner;
import com.legit2.Demigods.Libraries.ReflectCommand;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DUtil;

public class Zeus_deity implements Listener
{	
	// Create required universal deity variables
	private static final String DEITYNAME = "Zeus";
	private static final String DEITYALLIANCE = "God";
	private static final ChatColor DEITYCOLOR = ChatColor.YELLOW;

	/*
	 *  Set deity-specific ability variable(s).
	 */
	// "/shove" Command:
	private static String SHOVE_NAME = "Shove"; // Sets the name of this command
	private static long SHOVE_TIME; // Creates the variable for later use
	private static final int SHOVE_COST = 170; // Cost to run command in "favor"
	private static final int SHOVE_DELAY = 1500; // In milliseconds

	// "/lightning" Command:
	private static String LIGHTNING_NAME = "Lightning"; // Sets the name of this command
	private static long LIGHTNING_TIME; // Creates the variable for later use
	private static final int LIGHTNING_COST = 140; // Cost to run command in "favor"
	private static final int LIGHTNING_DELAY = 1000; // In milliseconds

	// "/storm" Command:
	private static String ULTIMATE_NAME = "Storm";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public String loadDeity()
	{
		DUtil.plugin.getServer().getPluginManager().registerEvents(this, DUtil.plugin);
		ULTIMATE_TIME = System.currentTimeMillis();
		SHOVE_TIME = System.currentTimeMillis();
		LIGHTNING_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}
	
	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		
		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		claimItems.add(Material.IRON_INGOT);
		claimItems.add(Material.FEATHER);
		
		return claimItems;
	}

	public ArrayList<String> getInfo(Player player)
	{		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		if(DUtil.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(ChatColor.YELLOW + "[Demigods] " + ChatColor.AQUA + DEITYNAME); //TODO
			toReturn.add(ChatColor.GREEN + "You are a follower of " + DEITYNAME + "!");
			
			return toReturn;
		}
		else
		{
			// Get Claim Item Names from ArrayList
			ArrayList<String> claimItemNames = new ArrayList<String>();
			for(Material item : getClaimItems())
			{
				claimItemNames.add(item.name());
			}
			
			// Make Claim Items readable.
			String claimItems = Joiner.on(", ").join(claimItemNames);
			
			toReturn.add(ChatColor.YELLOW + "[Demigods] " + ChatColor.AQUA + DEITYNAME); //TODO
			toReturn.add("Claim Items: " + claimItems);
			
			return toReturn;
		}
	}

	// This sets the particular passive ability for the Zeus_deity deity.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageEvent damageEvent)
	{
		if(damageEvent.getEntity() instanceof Player)
		{
			Player player = (Player)damageEvent.getEntity();
			if(!DUtil.canUseDeitySilent(player, DEITYNAME)) return;

			// If the player receives falling damage, cancel it
			if(damageEvent.getCause() == DamageCause.FALL)
			{
				damageEvent.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		int charID = DPlayerUtil.getCurrentChar(player);

		if(!DUtil.canUseDeitySilent(player, DEITYNAME)) return;

		if(DCharUtil.isEnabledAbility(player, SHOVE_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == DCharUtil.getBind(player, SHOVE_NAME))))
		{
			if(!DCharUtil.isCooledDown(player, SHOVE_NAME, SHOVE_TIME, false)) return;

			// Set the ability's delay
			SHOVE_TIME = System.currentTimeMillis() + SHOVE_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DCharUtil.getFavor(player, charID) >= SHOVE_COST)
			{
				shove(player);
				DCharUtil.subtractFavor(player, charID, SHOVE_COST);
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
				DCharUtil.disableAbility(player, SHOVE_NAME);
			}
		}
		
		if(DCharUtil.isEnabledAbility(player, LIGHTNING_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == DCharUtil.getBind(player, LIGHTNING_NAME))))
		{
			if(!DCharUtil.isCooledDown(player, LIGHTNING_NAME, LIGHTNING_TIME, false)) return;

			// Set the ability's delay
			LIGHTNING_TIME = System.currentTimeMillis() + LIGHTNING_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DCharUtil.getFavor(player, charID) >= LIGHTNING_COST)
			{
				lightning(player);
				DCharUtil.subtractFavor(player, charID, LIGHTNING_COST);
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
				DCharUtil.disableAbility(player, LIGHTNING_NAME);
			}
		}
	}

	/* ------------------
	 *  Command Handlers
	 * ------------------
	 *
	 *  Command: "/shove"
	 */
	@ReflectCommand.Command(name = "shove", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void shoveCommand(Player player, String arg1)
	{
		if(!DUtil.canUseDeity(player, DEITYNAME)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{		
			// Bind item
			DCharUtil.setBound(player, SHOVE_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(DCharUtil.isEnabledAbility(player, SHOVE_NAME))
			{
				DCharUtil.disableAbility(player, SHOVE_NAME);
				player.sendMessage(ChatColor.YELLOW + SHOVE_NAME + " is no longer active.");
			}
			else
			{
				DCharUtil.enableAbility(player, SHOVE_NAME);
				player.sendMessage(ChatColor.YELLOW + SHOVE_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		int charID = DPlayerUtil.getCurrentChar(player);
		int devotion = DCharUtil.getDevotion(player, charID);
		int targets = (int) Math.ceil(1.561 * Math.pow(devotion, 0.128424));
		double multiply = 0.1753 * Math.pow(devotion, 0.322917);
		
		if(!DUtil.canLocationPVP(player.getLocation())) player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
		
		// Get Targets as an ArrayList
		ArrayList<LivingEntity> hit = new ArrayList<LivingEntity>();
		LivingEntity target = DUtil.autoTarget(player);
		
		if(target == null)
		{
			player.sendMessage(ChatColor.YELLOW + "No target found.");
			return;
		}
		
		for (LivingEntity livingEntity : player.getWorld().getLivingEntities())
		{
			if(targets == hit.size()) break;
			
			if(livingEntity instanceof Player)
			{
				if(DUtil.areAllied(player, (Player) livingEntity)) continue;
			}
			
			if((livingEntity.equals(target)) && !hit.contains(livingEntity)) if (DUtil.canTarget(livingEntity, livingEntity.getLocation())) hit.add(livingEntity);
		}
		
		if (hit.size() > 0)
		{
			for (LivingEntity livingEntity : hit)
			{
				Vector vector = player.getLocation().toVector();
				Vector victor = livingEntity.getLocation().toVector().subtract(vector);
				victor.multiply(multiply);
				livingEntity.setVelocity(victor);
			}
		}
	}
	
	/*
	 *  Command: "/lightning"
	 */
	
	@ReflectCommand.Command(name = "lightning", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void lightningCommand(Player player, String arg1)
	{		
		if(!DUtil.canUseDeity(player, DEITYNAME)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{		
			// Bind item
			DCharUtil.setBound(player, LIGHTNING_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(DCharUtil.isEnabledAbility(player, LIGHTNING_NAME)) 
			{
				DCharUtil.disableAbility(player, LIGHTNING_NAME);
				player.sendMessage(ChatColor.YELLOW + LIGHTNING_NAME + " is no longer active.");
			}
			else
			{
				DCharUtil.enableAbility(player, LIGHTNING_NAME);
				player.sendMessage(ChatColor.YELLOW + LIGHTNING_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void lightning(Player player)
	{
		// Define variables
		LivingEntity target = DUtil.autoTarget(player);
		
		if(!DUtil.canLocationPVP(player.getLocation())) player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
		
		if(target == null)
		{
			player.sendMessage(ChatColor.YELLOW + "No target found.");
			return;
		}
		
		if(target instanceof Player)
		{
			if(DUtil.areAllied(player, (Player) target)) return;
		}
		
		if(target.equals(target)) if (DUtil.canTarget(target, target.getLocation())) strikeLightning(player, target);
	}

	/*
	 *  Command: "/storm"
	 */
	@ReflectCommand.Command(name = "storm", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")
	public static void ultimateCommand(Player player)
	{
		// Set variables
		int charID = DPlayerUtil.getCurrentChar(player);
		
		// Check the player for DEITYNAME
		if(!DCharUtil.hasDeity(player, DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000)))/60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000))%60)+" seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(DCharUtil.getFavor(player, charID) >= ULTIMATE_COST)
		{
			if(!DUtil.canLocationPVP(player.getLocation()))
			{
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return; 
			}
			
			player.sendMessage(ChatColor.YELLOW + "Zeus has struck " + storm(player) + " targets!");

			// Set favor and cooldown
			DCharUtil.subtractFavor(player, charID, ULTIMATE_COST);
			player.setNoDamageTicks(1000);
			int cooldownMultiplier = (int)(ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN)*((double)DCharUtil.getAscensions(player, charID) / 100)));
			ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
		}
		// Give a message if there is not enough favor
		else player.sendMessage(ChatColor.YELLOW + ULTIMATE_NAME + " requires " + ULTIMATE_COST + ChatColor.GREEN + " favor" + ChatColor.YELLOW + ".");
	}
	
	// The actual ability command
	public static int storm(Player player)
	{
		// Define variables
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		Vector playerLocation = player.getLocation().toVector();
		
		if(!DUtil.canLocationPVP(player.getLocation())) player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
		
		for(Entity anEntity : player.getWorld().getEntities()) if(anEntity.getLocation().toVector().isInSphere(playerLocation, 50.0)) entityList.add(anEntity);

		int count = 0;
		for(Entity entity : entityList)
		{
			try
			{
				if(entity instanceof Player)
				{
					Player otherPlayer = (Player) entity;
					if (!DUtil.areAllied(player, otherPlayer) && !otherPlayer.equals(player))
					{
						strikeLightning(player, otherPlayer);
						strikeLightning(player, otherPlayer);
						strikeLightning(player, otherPlayer);
						count++;
					}
				}
				else if(entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					strikeLightning(player, livingEntity);
					strikeLightning(player, livingEntity);
					strikeLightning(player, livingEntity);
					count++;
				}
			}
			catch (Exception notAlive) {} //ignore stuff like minecarts
		}
		
		return count;
	}

	private static void strikeLightning(Player player, LivingEntity target)
	{
		// Set variables
		int charID = DPlayerUtil.getCurrentChar(player);
		
		if(!player.getWorld().equals(target.getWorld())) return;
		if(!DUtil.canTarget(target, target.getLocation())) return;
		
		player.getWorld().strikeLightningEffect(target.getLocation());
		
		for(Entity entity : target.getLocation().getBlock().getChunk().getEntities())
		{
			if(entity instanceof LivingEntity)
			{
				LivingEntity livingEntity = (LivingEntity) entity;
				if(livingEntity.getLocation().distance(target.getLocation()) < 1.5) DUtil.customDamage(player, livingEntity, DCharUtil.getAscensions(player, charID)*2, DamageCause.LIGHTNING);
			}
		}
	}
	
	// Don't touch these, they're required to work.
	public static String getName() { return DEITYNAME; }
	public static String getAlliance() { return DEITYALLIANCE; }
	public static ChatColor getColor() { return DEITYCOLOR; }
}