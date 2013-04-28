package com.censoredsoftware.Demigods.Deity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.Demigods.API.AbilityAPI;
import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.MiscAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Event.Ability.AbilityEvent;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;
import com.google.common.base.Joiner;

public class Template implements Deity, Listener
{
	// Create required universal deity variables
	private static final String DEITYNAME = "Template";
	private static final String DEITYALLIANCE = "Test";
	private static final ChatColor DEITYCOLOR = ChatColor.BLACK;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/testabil" Command:
	private static final String TEST_NAME = "Testabil"; // Sets the name of this command
	private static final int TEST_COST = 170; // Cost to run command in "favor"
	private static final int TEST_DELAY = 0; // In milliseconds

	// "/testult" Command:
	private static final String ULTIMATE_NAME = "Testult";
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	@Override
	public List<Material> getClaimItems()
	{
		List<Material> claimItems = new ArrayList<Material>();

		claimItems.add(Material.BEDROCK);

		return claimItems;
	}

	@Override
	public List<String> getInfo(Player player)
	{
		List<String> toReturn = new ArrayList<String>();

		if(MiscAPI.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(ChatColor.YELLOW + "[Demigods] " + DEITYCOLOR + DEITYNAME); // TODO
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

			toReturn.add(ChatColor.YELLOW + "[Demigods] " + DEITYCOLOR + DEITYNAME); // TODO
			toReturn.add("Claim Items: " + claimItems);

			return toReturn;
		}
	}

	// This sets the particular passive ability for the Template deity.
	// Whether or not this is used, along with other things, will vary depending
	// on the particular deity you're creating.
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
				damageEvent.setDamage(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

		if(character.getAbilities().isEnabledAbility(TEST_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBindings().getBind(TEST_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, TEST_NAME, true)) return;

			testabil(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/test"
	 */
	public static void testCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.getBindings().setBound(TEST_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.getAbilities().isEnabledAbility(TEST_NAME))
			{
				character.getAbilities().toggleAbility(TEST_NAME, false);
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is no longer active.");
			}
			else
			{
				character.getAbilities().toggleAbility(TEST_NAME, true);
				player.sendMessage(ChatColor.YELLOW + TEST_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void testabil(Player player)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!AbilityAPI.doAbilityPreProcess(player, "testabil", TEST_COST, AbilityEvent.AbilityType.PASSIVE)) return;
		CharacterAPI.setCoolDown(player, TEST_NAME, System.currentTimeMillis() + TEST_DELAY);
		character.subtractFavor(TEST_COST);

		player.sendMessage(ChatColor.YELLOW + "You just used the \"" + TEST_NAME.toLowerCase() + "\" ability!");
	}

	/*
	 * Command: "/testult"
	 */
	public static void testultCommand(Player player, String[] args)
	{
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")) return;

		// Set variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(CharacterAPI.isCooledDown(player, ULTIMATE_NAME, false))
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ((((CharacterAPI.getCoolDown(player, ULTIMATE_NAME)) / 1000) - (System.currentTimeMillis() / 1000))) / 60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ((((CharacterAPI.getCoolDown(player, ULTIMATE_NAME)) / 1000) - (System.currentTimeMillis() / 1000)) % 60) + " seconds.");
			return;
		}

		if(!AbilityAPI.doAbilityPreProcess(player, "testult", ULTIMATE_COST, AbilityEvent.AbilityType.PASSIVE)) return;
		character.subtractFavor(ULTIMATE_COST);

		player.sendMessage(ChatColor.YELLOW + "You just used the ultimate, " + ULTIMATE_NAME + ", for " + DEITYNAME + "!");

		testHelper(player);

		// Set favor and cooldown
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		CharacterAPI.setCoolDown(player, ULTIMATE_NAME, System.currentTimeMillis() + cooldownMultiplier * 1000);
	}

	/*
	 * Command Helper Methods
	 */
	private static void testHelper(Player player)
	{
		player.sendMessage(ChatColor.YELLOW + "This command called a helper!");
	}

	@Override
	public String loadDeity()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Demigods.demigods);
		return DEITYNAME + " loaded.";
	}

	@Override
	public List<String> getCommands()
	{
		List<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("test");
		COMMANDS.add("testult");

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
