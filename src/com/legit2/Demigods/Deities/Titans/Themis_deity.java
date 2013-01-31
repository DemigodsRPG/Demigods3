package com.legit2.Demigods.Deities.Titans;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.base.Joiner;
import com.legit2.Demigods.Libraries.ReflectCommand;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DMiscUtil;

public class Themis_deity implements Listener
{	
	// Create required universal deity variables
	private static final String DEITYNAME = "Themis";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.GRAY;

	/*
	 *  Set deity-specific ability variable(s).
	 */
	// "/swap" Command:
	private static String SWAP_NAME = "Swap"; // Sets the name of this command
	private static long SWAP_TIME; // Creates the variable for later use
	private static final int SWAP_COST = 310; // Cost to run command in "favor"
	private static final int SWAP_DELAY = 2400; // In milliseconds

	// "/congregate" Command:
	private static String ULTIMATE_NAME = "Congregate";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 6000; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 1200; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 500; // In seconds

	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		
		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		claimItems.add(Material.COMPASS);
		claimItems.add(Material.PAPER);
		
		return claimItems;
	}

	public ArrayList<String> getInfo(Player player)
	{		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		if(DMiscUtil.canUseDeitySilent(player, DEITYNAME))
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		int charID = DPlayerUtil.getCurrentChar(player);

		if(!DMiscUtil.canUseDeitySilent(player, DEITYNAME)) return;

		if(DCharUtil.isEnabledAbility(player, SWAP_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == DCharUtil.getBind(player, SWAP_NAME))))
		{
			if(!DCharUtil.isCooledDown(player, SWAP_NAME, SWAP_TIME, false)) return;

			// Set the ability's delay
			SWAP_TIME = System.currentTimeMillis() + SWAP_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DCharUtil.getFavor(charID) >= SWAP_COST)
			{
				swap(player);
				DCharUtil.subtractFavor(charID, SWAP_COST);
			}
			else
			{
				player.sendMessage(ChatColor.GRAY + "You do not have enough favor.");
			}
		}
	}

	/* ------------------
	 *  Command Handlers
	 * ------------------
	 *
	 *  Command: "/swap"
	 */
	@ReflectCommand.Command(name = "swap", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void swapCommand(Player player, String arg1)
	{
		if(!DMiscUtil.canUseDeity(player, DEITYNAME)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{		
			// Bind item
			DCharUtil.setBound(player, SWAP_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(DCharUtil.isEnabledAbility(player, SWAP_NAME))
			{
				DCharUtil.disableAbility(player, SWAP_NAME);
				player.sendMessage(ChatColor.YELLOW + SWAP_NAME + " is no longer active.");
			}
			else
			{
				DCharUtil.enableAbility(player, SWAP_NAME);
				player.sendMessage(ChatColor.YELLOW + SWAP_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void swap(Player player)
	{
		// Define variables
		Location between = player.getLocation();
		LivingEntity target = DMiscUtil.autoTarget(player);
		
		if(target == null) return;
		if(!DMiscUtil.canTarget(player, player.getLocation())) return;
		if(!DMiscUtil.canTarget(target, target.getLocation())) return;

		player.teleport(target.getLocation());
		target.teleport(between);
	}
	
	/*
	 *  Command: "/assemble"
	 */
	@ReflectCommand.Command(name = "assemble", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void assembleCommand(Player player, String arg1)
	{
		int charID = DPlayerUtil.getCurrentChar(player);
		
		if(!DCharUtil.isImmortal(player)) return;
		if(!DDataUtil.hasCharData(charID, "temp_themis_congregate")) return;
		for(Player pl : player.getWorld().getPlayers())
		{
			if(DCharUtil.isImmortal(pl) && DDataUtil.hasCharData(DPlayerUtil.getCurrentChar(pl), "temp_themis_congregate_call"))
			{
				DDataUtil.removeCharData(charID, "temp_themis_congregate");
				player.teleport(pl.getLocation());
				return;
			}
		}
		player.sendMessage(ChatColor.YELLOW + "Unable to reach the congregation's location.");
	}

	/*
	 *  Command: "/congregate"
	 */
	@ReflectCommand.Command(name = "congregate", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")
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
			if(!DMiscUtil.canLocationPVP(player.getLocation()))
			{
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return; 
			}
			
			int n = congregate(player);
			if(n > 0) player.sendMessage(ChatColor.YELLOW + "Themis has called upon " + n + " players!");
			else player.sendMessage(ChatColor.YELLOW + "There are no players to congregate.");
			

			// Set favor and cooldown
			DCharUtil.subtractFavor(charID, ULTIMATE_COST);
			player.setNoDamageTicks(1000);
			int cooldownMultiplier = (int)(ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN)*((double)DCharUtil.getAscensions(charID) / 100)));
			ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
		}
		// Give a message if there is not enough favor
		else player.sendMessage(ChatColor.YELLOW + ULTIMATE_NAME + " requires " + ULTIMATE_COST + ChatColor.GREEN + " favor" + ChatColor.YELLOW + ".");
	}
	
	// The actual ability command
	public static int congregate(Player player)
	{
		// Define variables
		int charID = DPlayerUtil.getCurrentChar(player);
		
		DDataUtil.saveTimedCharData(charID, "temp_themis_congregate_call", true, 60);
		
		int count = 0;	
		for(Player pl : player.getWorld().getPlayers())
		{
			if (DCharUtil.isImmortal(pl))
			{
				count++;
				if(!player.equals(pl) && !DDataUtil.hasCharData(charID, "temp_themis_congregate"))
				{
					pl.sendMessage(ChatColor.GOLD + "Themis has called for an assembly of deities at " + player.getName() + "'s location.");
					pl.sendMessage(ChatColor.GOLD + "Type " + ChatColor.WHITE+"/assemble" + ChatColor.GOLD + " to be teleported.");
					pl.sendMessage(ChatColor.GOLD + "You will be immune to damage upon arrival for a short time.");
					pl.sendMessage(ChatColor.GRAY + "You have one minute to answer the invitation.");
					pl.sendMessage(ChatColor.GRAY + "To see how much time is left to respond, use " + ChatColor.WHITE + "qd" + ChatColor.GRAY + ".");
					DDataUtil.saveTimedCharData(charID, "temp_themis_congregate", true, 60);
				}
			}
		}
		
		return count;
	}
	
	// Don't touch these, they're required to work.
	public String loadDeity()
	{
		DMiscUtil.plugin.getServer().getPluginManager().registerEvents(this, DMiscUtil.plugin);
		ULTIMATE_TIME = System.currentTimeMillis();
		SWAP_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}
	public static String getName() { return DEITYNAME; }
	public static String getAlliance() { return DEITYALLIANCE; }
	public static ChatColor getColor() { return DEITYCOLOR; }
}