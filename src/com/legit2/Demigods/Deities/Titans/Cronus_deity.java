package com.legit2.Demigods.Deities.Titans;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.base.Joiner;
import com.legit2.Demigods.Libraries.ReflectCommand;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DUtil;

public class Cronus_deity implements Listener
{	
	// Create required universal deity variables
	private static final String DEITYNAME = "Cronus";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.DARK_PURPLE;

	/*
	 *  Set deity-specific ability variable(s).
	 */
	// "/cleave" Command:
	private static String CLEAVE_NAME = "Cleave"; // Sets the name of this command
	private static long CLEAVE_TIME; // Creates the variable for later use
	private static final int CLEAVE_COST = 100; // Cost to run command in "favor"
	private static final int CLEAVE_DELAY = 1000; // In milliseconds

	// "/slow" Command:
	private static String SLOW_NAME = "Slow"; // Sets the name of this command
	private static long SLOW_TIME; // Creates the variable for later use
	private static final int SLOW_COST = 180; // Cost to run command in "favor"
	private static final int SLOW_DELAY = 1000; // In milliseconds

	// "/timestop" Command:
	private static String ULTIMATE_NAME = "Timestop";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds
	
	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		
		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		claimItems.add(Material.SOUL_SAND);
		claimItems.add(Material.WATCH);
		
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

	// This sets the particular passive ability for the Cronus deity.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageByEntityEvent damageEvent)
	{
		if(damageEvent.getDamager() instanceof Player)
		{
			Player player = (Player)damageEvent.getDamager();
			int charID = DPlayerUtil.getCurrentChar(player);
			
			if(!DUtil.canUseDeitySilent(player, DEITYNAME)) return;
			
			if(!DUtil.canLocationPVP(damageEvent.getEntity().getLocation())) return;

			if(!player.getItemInHand().getType().name().contains("_HOE")) return;
			
			if(damageEvent.getEntity() instanceof Player)
			{
				Player attacked = (Player)damageEvent.getEntity();
				
				// Cronus Passive: Stop movement
				if(!DCharUtil.isImmortal(attacked) || (DCharUtil.isImmortal(attacked) && !DUtil.areAllied(player, attacked))) attacked.setVelocity(new Vector(0,0,0));
			}
			
			if(DCharUtil.isEnabledAbility(player, CLEAVE_NAME))
			{
				if(!DCharUtil.isCooledDown(player, CLEAVE_NAME, CLEAVE_TIME, false)) return;

				// Set the ability's delay
				CLEAVE_TIME = System.currentTimeMillis() + CLEAVE_DELAY;
				
				// Check to see if player has enough favor to perform ability
				if(DCharUtil.getFavor(charID) >= CLEAVE_COST)
				{
					cleave(damageEvent);
					DCharUtil.subtractFavor(charID, CLEAVE_COST);
					return;
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW + "You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
					DCharUtil.disableAbility(player, CLEAVE_NAME);
				}
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

		if(DCharUtil.isEnabledAbility(player, SLOW_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == DCharUtil.getBind(player, SLOW_NAME))))
		{
			if(!DCharUtil.isCooledDown(player, SLOW_NAME, SLOW_TIME, false)) return;

			// Set the ability's delay
			SLOW_TIME = System.currentTimeMillis() + SLOW_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DCharUtil.getFavor(charID) >= SLOW_COST)
			{
				slow(player);
				DCharUtil.subtractFavor(charID, SLOW_COST);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
				DCharUtil.disableAbility(player, SLOW_NAME);
			}
		}
	}

	/* ------------------
	 *  Command Handlers
	 * ------------------
	 *
	 *  Command: "/cleave"
	 */
	@ReflectCommand.Command(name = "cleave", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void cleaveCommand(Player player)
	{
		if(!DUtil.canUseDeity(player, DEITYNAME)) return;

		if(DCharUtil.isEnabledAbility(player, CLEAVE_NAME))
		{
			DCharUtil.disableAbility(player, CLEAVE_NAME);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is no longer active.");
		}
		else
		{
			DCharUtil.enableAbility(player, CLEAVE_NAME);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is now active.");
		}
	}

	// The actual ability command
	public static void cleave(EntityDamageByEntityEvent damageEvent)
	{
		// Define variables
		Player player = (Player)damageEvent.getDamager();
		int charID = DPlayerUtil.getCurrentChar(player);
		Entity attacked = damageEvent.getEntity();
		
		if (DCharUtil.getFavor(charID) >= CLEAVE_COST)
		{
			if (!(attacked instanceof LivingEntity)) return;
			
			for (int i = 1; i <= 31; i += 4) attacked.getWorld().playEffect(attacked.getLocation(), Effect.SMOKE, i);
			
			DUtil.customDamage(player, (LivingEntity)attacked, (int)Math.ceil(Math.pow(DCharUtil.getDevotion(charID), 0.35)), DamageCause.ENTITY_ATTACK);
			
			if ((LivingEntity)attacked instanceof Player)
			{
				Player attackedPlayer = (Player)((LivingEntity)attacked);
				
				attackedPlayer.setFoodLevel(attackedPlayer.getFoodLevel() - (damageEvent.getDamage()/2));
				
				if (attackedPlayer.getFoodLevel() < 0) attackedPlayer.setFoodLevel(0);
			}
		}
	}
	
	/*
	 *  Command: "/slow"
	 */
	
	@ReflectCommand.Command(name = "slow", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void slowCommand(Player player, String arg1)
	{		
		if(!DUtil.canUseDeity(player, DEITYNAME)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{		
			// Bind item
			DCharUtil.setBound(player, SLOW_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(DCharUtil.isEnabledAbility(player, SLOW_NAME))
			{
				DCharUtil.disableAbility(player, SLOW_NAME);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is no longer active.");
			}
			else
			{
				DCharUtil.enableAbility(player, SLOW_NAME);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void slow(Player player)
	{
		// Define variables
		int charID = DPlayerUtil.getCurrentChar(player);
		int devotion = DCharUtil.getDevotion( charID);
		int duration = (int) Math.ceil(3.635 * Math.pow(devotion, 0.2576)); //seconds
		int strength = (int) Math.ceil(1.757 * Math.pow(devotion, 0.097));
		Player target = null; 
		if(DUtil.autoTarget(player) instanceof Player) target = (Player) DUtil.autoTarget(player);
		
		if(DUtil.areAllied(player, (Player) target) || !DUtil.canTarget(target, target.getLocation()))  return;
		
		if ((target != null) && (target.getEntityId() != player.getEntityId()))
		{
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, strength));
			player.sendMessage(ChatColor.YELLOW + target.getName() + " has been slowed.");
			target.sendMessage(ChatColor.RED + "You have been slowed for " + duration + " seconds.");
			
			// DUtil.setPlayerData(target.getName(), "slow", duration);
		}
		else
		{
			player.sendMessage(ChatColor.YELLOW + "No target found.");
		}
	}

	/*
	 *  Command: "/timestop"
	 */
	@ReflectCommand.Command(name = "timestop", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")
	public static void ultimateCommand(Player player)
	{
		// Set variables
		int charID = DPlayerUtil.getCurrentChar(player);
		
		// Check the player for DEITYNAME
		if(!DCharUtil.hasDeity(charID, DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000)))/60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000))%60)+" seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(DCharUtil.getFavor(charID) >= ULTIMATE_COST)
		{
			if(!DUtil.canLocationPVP(player.getLocation()))
			{
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return; 
			}
			
			int duration = (int) Math.round(9.9155621 * Math.pow(DCharUtil.getAscensions(charID), 0.459019));
			player.sendMessage(ChatColor.YELLOW + "Cronus has stopped time for " + duration + " seconds, for " + timestop(player, duration) + " enemies!");

			// Set favor and cooldown
			DCharUtil.subtractFavor(charID, ULTIMATE_COST);
			player.setNoDamageTicks(1000);
			int cooldownMultiplier = (int)(ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN)*((double) DCharUtil.getAscensions(charID) / 100)));
			ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
		}
		// Give a message if there is not enough favor
		else player.sendMessage(ChatColor.YELLOW + ULTIMATE_NAME + " requires " + ULTIMATE_COST + ChatColor.GREEN + " favor" + ChatColor.YELLOW + ".");
	}
	
	// The actual ability command
	public static int timestop(Player player, int duration)
	{
		// Define variables
		int charID = DPlayerUtil.getCurrentChar(player);

		int slowamount = (int)Math.round(4.77179 * Math.pow(DCharUtil.getAscensions(charID), 0.17654391));
		int count = 0;
		
		for(Player onlinePlayer : player.getWorld().getPlayers())
		{
			if(!(onlinePlayer.getLocation().toVector().isInSphere(player.getLocation().toVector(), 70))) continue;
			
			if(!DUtil.canLocationPVP(onlinePlayer.getLocation())) continue;
			
			if (DCharUtil.isImmortal(onlinePlayer) && DUtil.areAllied(player, onlinePlayer)) continue;

			onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, slowamount));
			
			//DUtil.setPlayerDamage(username, "timestop", duration);
			
			count++;
		}
		
		return count;
	}
	
	// Don't touch these, they're required to work.
	public String loadDeity()
	{
		DUtil.plugin.getServer().getPluginManager().registerEvents(this, DUtil.plugin);
		ULTIMATE_TIME = System.currentTimeMillis();
		CLEAVE_TIME = System.currentTimeMillis();
		SLOW_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}
	public static String getName() { return DEITYNAME; }
	public static String getAlliance() { return DEITYALLIANCE; }
	public static ChatColor getColor() { return DEITYCOLOR; }
}