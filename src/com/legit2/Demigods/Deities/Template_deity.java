package com.legit2.Demigods.Deities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.legit2.Demigods.DUtil;
import com.legit2.Demigods.Libraries.ReflectCommand;

public class Template_deity implements Listener
{	
	// Create required universal deity variables
	private static final String DEITYNAME = "Template";
	private static final String DEITYALLIANCE = "Test";

	/*
	 *  Set deity-specific ability variable(s).
	 */
	// "/testabil" Command:
	private static String TEST_NAME = "Testabil"; // Sets the name of this command
	private static long TEST_TIME; // Creates the variable for later use
	private static final int TEST_COST = 170; // Cost to run command in "favor"
	private static final int TEST_DELAY = 0; // In milliseconds

	// "/testult" Command:
	private static String ULTIMATE_NAME = "Testult";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public Template_deity()
	{
		ULTIMATE_TIME = System.currentTimeMillis();
		TEST_TIME = System.currentTimeMillis();
	}
	
	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		
		// Add new items in this format: claimItems.add(Material. NAME_OF_MATERIAL );

		return claimItems;
	}

	public void printInfo(Player player)
	{
		// Set variables
		String username = player.getName();
		
		if(DUtil.hasDeity(username, DEITYNAME) && DUtil.isImmortal(username))
		{
			// Print Deity Info to Chat
			DUtil.taggedMessage(player, ChatColor.AQUA + DEITYNAME);
			// TODO Deity Info
			return;
		}

		DUtil.taggedMessage(player, ChatColor.AQUA + DEITYNAME);
		// TODO Deity Info
	}

	// This sets the particular passive ability for the Template_deity deity.
	// Whether or not this is used, along with other things, will vary depending
	// on the particular deity you're creating.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageEvent damageEvent)
	{
		if(damageEvent.getEntity() instanceof Player)
		{
			Player player = (Player)damageEvent.getEntity();
			if(!DUtil.hasDeity(player.getName(), DEITYNAME) || !DUtil.isImmortal(player.getName())) return;

			// If the player receives falling damage, cancel it
			if(damageEvent.getCause() == DamageCause.FALL)
			{
				damageEvent.setDamage(0);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		String username = player.getName();

		if(!DUtil.hasDeity(username, DEITYNAME) || !DUtil.isImmortal(username)) return;

		if(((player.getItemInHand() != null) && (player.getItemInHand().getType() == DUtil.getDeityData(username, DEITYNAME, TEST_NAME + "_bind"))))
		{
			if(TEST_TIME > System.currentTimeMillis()) return;

			// Set the ability's delay
			TEST_TIME = System.currentTimeMillis() + TEST_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DUtil.getFavor(username) >= TEST_COST)
			{
				testabil(player);
				DUtil.subtractFavor(username, TEST_COST);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
				DUtil.setPlayerData(username, TEST_NAME, false);
			}
		}
	}

	/* ------------------
	 *  Command Handlers
	 * ------------------
	 *
	 *  Command: "/test"
	 */
	@ReflectCommand.Command(name = "testabil", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void testCommand(Player player, String arg1)
	{
		// Set variables
		String username = player.getName();
		
		if(!canUseDeity(player)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{			
			if(DUtil.getDeityData(username, DEITYNAME, TEST_NAME + "_bind") == null)
			{
				if(player.getItemInHand().getType() == Material.AIR)
				{
					player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
				}
				else
				{
					DUtil.setBound(username, player.getItemInHand().getType());
					DUtil.setDeityData(username, DEITYNAME, TEST_NAME + "_bind", player.getItemInHand().getType());
					player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is now bound to: " + player.getItemInHand().getType().name().toLowerCase());
				}
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is no longer bound to: " + ((Material) DUtil.getDeityData(username, DEITYNAME, TEST_NAME + "_bind")).name().toLowerCase());
				DUtil.removeBind(username, (Material)DUtil.getDeityData(username, DEITYNAME, TEST_NAME + "_bind"));
				DUtil.removeDeityData(username, DEITYNAME, TEST_NAME + "_bind");
			}
		}
		else
		{
			if(DUtil.getPlayerData(username, TEST_NAME) != null && (Boolean) DUtil.getPlayerData(username, TEST_NAME)) 
			{
				DUtil.setPlayerData(username, TEST_NAME, false);
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is no longer active.");
			}
			else
			{
				DUtil.setPlayerData(username, TEST_NAME, true);
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void testabil(Player player)
	{
		player.sendMessage(ChatColor.YELLOW + "You just used the \"" + TEST_NAME.toLowerCase() + "\" ability!");
	}

	/*
	 *  Command: "/testult"
	 */
	@ReflectCommand.Command(name = "testult", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.god." + DEITYNAME + ".ultimate")
	public static void ultimateCommand(Player player)
	{
		// Set variables
		String username = player.getName();
		
		// Check the player for DEITYNAME
		if(!DUtil.hasDeity(username, DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000)))/60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and "+((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000))%60)+" seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(DUtil.getFavor(username) >= ULTIMATE_COST)
		{
			if(!DUtil.canPVP(player.getLocation()))
			{
				testHelper(player);
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return; 
			}

			player.sendMessage(ChatColor.YELLOW + "You just used the ultimate for " + DEITYNAME + "!");

			// Set favor and cooldown
			DUtil.subtractFavor(username, ULTIMATE_COST);
			player.setNoDamageTicks(1000);
			int cooldownMultiplier = (int)(ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN)*((double)DUtil.getAscensions(username) / 100)));
			ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
		}
		// Give a message if there is not enough favor
		else player.sendMessage(ChatColor.YELLOW + ULTIMATE_NAME + " requires " + ULTIMATE_COST + ChatColor.GREEN + " favor" + ChatColor.RESET + ".");
	}

	/*
	 * Command Helper Methods
	 */
	private static void testHelper(Player player)
	{
		player.sendMessage(ChatColor.YELLOW + "This command called a helper!");
	}

	// Don't touch these, they're required to work.
	public String getName() { return DEITYNAME; }
	public String getAlliance() { return DEITYALLIANCE; }
	public String loadDeity() { DUtil.plugin.getServer().getPluginManager().registerEvents(this, DUtil.plugin); return DEITYNAME + " loaded."; }
	public static boolean canUseDeity(Player player)
	{		
		// Check the player for DEITYNAME
		if(!DUtil.hasDeity(player.getName(), DEITYNAME))
		{
			player.sendMessage(ChatColor.RED + "You haven't even claimed " + DEITYNAME + "! You can't do that!");
			return false;
		}
		else if(!DUtil.isImmortal(player.getName()))
		{
			player.sendMessage(ChatColor.RED + "You can't do that, mortal!");
			return false;
		}
		return true;
	}
}