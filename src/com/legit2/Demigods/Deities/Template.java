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
import com.legit2.Demigods.ReflectCommand;

public class Template implements Deity,Listener
{
	private static final long serialVersionUID = 2242753324910371936L;

	// Create required universal deity variables
	private static final String DEITYNAME = "Template";
	private static final String DEITYALLIANCE = "Test";

	/*
	 *  Set deity-specific ability variable(s).
	 */
	// "/test" Command:
	private static String TEST_NAME = "Testcommand"; // Sets the command to not being inactive
	private static boolean ABILITY1 = false; // Sets the command to not being inactive
	private static long TEST_TIME; 
	private static final int TEST_COST = 170; // Cost to run command in "favor"
	private static final int TEST_DELAY = 1500; // In milliseconds
	private static Material TEST_BIND = null; // Sets the bind material to false for the command

	// "/ultimate" Command:
	private static String ULTIMATE_NAME = "Storm";
	private static long ULTIMATE_TIME;
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public Template()
	{
		ULTIMATE_TIME = System.currentTimeMillis();
		TEST_TIME = System.currentTimeMillis();
	}
	
	@Override
	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		claimItems.add(Material.IRON_INGOT);

		return claimItems;
	}

	@Override
	public void printInfo(Player player)
	{
		if(DUtil.hasDeity(player.getName(), DEITYNAME) && DUtil.isImmortal(player))
		{
			// Print Deity Info to Chat
			DUtil.taggedMessage(player, ChatColor.AQUA + DEITYNAME);
			// TODO Deity Info
			return;
		}

		DUtil.taggedMessage(player, ChatColor.AQUA + DEITYNAME);
		// TODO Deity Info
	}

	// This sets the particular passive ability for the Template deity.
	// Whether or not this is used, along with other things, will vary depending
	// on the particular deity you're creating.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageEvent damageEvent)
	{
		if(damageEvent.getEntity() instanceof Player)
		{
			Player player = (Player)damageEvent.getEntity();
			if(!DUtil.hasDeity(player.getName(), DEITYNAME) || !DUtil.isImmortal(player)) return;

			// If the player receives falling damage, cancel it
			if(damageEvent.getCause()==DamageCause.FALL)
			{
				damageEvent.setDamage(0);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		Player player = interactEvent.getPlayer();

		if(!DUtil.hasDeity(player.getName(), DEITYNAME) || !DUtil.isImmortal(player)) return;

		if(ABILITY1 || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == TEST_BIND)))
		{
			if(TEST_TIME > System.currentTimeMillis()) return;

			// Set the ability's delay
			TEST_TIME = System.currentTimeMillis() + TEST_DELAY;

			// Check to see if player has enough favor to perform ability
			if(DUtil.getFavor(player.getName()) >= TEST_COST)
			{
				test(player);
				DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName()) - TEST_COST);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW+"You do not have enough " + ChatColor.GREEN + "favor" + ChatColor.RESET + ".");
				DUtil.setData(player.getName(), "shove", false);
			}
		}
	}

	/* ------------------
	 *  Command Handlers
	 * ------------------
	 */

	/*
	 *  Command: "/test"
	 */
	@ReflectCommand.Command(name = "test", sender = ReflectCommand.Sender.PLAYER, permission = "demigods." + DEITYALLIANCE + "." + DEITYNAME)
	public static void testCommand(Player player, String arg1)
	{
		// Check the player for DEITYNAME
		if(!DUtil.hasDeity(player.getName(), DEITYNAME)) return;

		if(arg1.equalsIgnoreCase("bind"))
		{
			if(TEST_BIND == null)
			{
				DUtil.isBound(player, player.getItemInHand().getType());
				if(player.getItemInHand().getType() == Material.AIR) player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
				else
				{
					DUtil.setBind(player, player.getItemInHand().getType());
					TEST_BIND = player.getItemInHand().getType();
					player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is now bound to a(n) " + player.getItemInHand().getType().name().toLowerCase() + ".");
				}
			}
			else
			{
				DUtil.removeBind(player, TEST_BIND);
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is no longer bound to " + TEST_BIND.name() + ".");
				TEST_BIND = null;
			}
		}

		if(DUtil.getData(player.getName(), TEST_NAME) != null && (Boolean) DUtil.getData(player.getName(), TEST_NAME)) 
		{
			DUtil.setData(player.getName(), TEST_NAME, false);
			player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is no longer active.");
		}
		else
		{
			DUtil.setData(player.getName(), TEST_NAME, true);
			player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is now active.");
		}
	}

	// The actual ability command
	public static void test(Player player)
	{
		player.sendMessage(ChatColor.YELLOW + "You just used the \"" + TEST_NAME.toLowerCase() + "\" ability!");
	}

	/*
	 *  Command: "/storm"
	 */
	@ReflectCommand.Command(name = "storm", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.god." + DEITYNAME + ".ultimate")
	public static void ultimateCommand(Player player)
	{
		// Check the player for DEITYNAME
		if(!DUtil.hasDeity(player.getName(), DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000)))/60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and "+((((ULTIMATE_TIME)/1000)-(System.currentTimeMillis()/1000))%60)+" seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(DUtil.getFavor(player.getName()) >= ULTIMATE_COST)
		{
			if(!DUtil.canPVP(player.getLocation()))
			{
				testHelper(player);
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return; 
			}

			player.sendMessage(ChatColor.YELLOW + "You just used the ultimate for " + DEITYNAME + "!");

			// Set favor and cooldown
			DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName()) - ULTIMATE_COST);
			player.setNoDamageTicks(1000);
			int cooldownMultiplier = (int)(ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN)*((double)DUtil.getAscensions(player.getName()) / 100)));
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
	@Override
	public void onTick(long timeSent) {}
	@Override
	public String getName() { return DEITYNAME; }
	@Override
	public String getAlliance() { return DEITYALLIANCE; }
	public String loadDeity() { DUtil.plugin.getServer().getPluginManager().registerEvents(this, DUtil.plugin); return DEITYNAME + " loaded."; }
}