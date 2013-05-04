package com.censoredsoftware.Demigods.Demo.Data.Deity.Titan;

import java.util.ArrayList;
import java.util.List;

import com.censoredsoftware.Demigods.Engine.Demigods;
import org.bukkit.Bukkit;
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

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class Cronus_deity extends Deity implements Listener
{
	// Create required universal deity variables
	private static final String DEITYNAME = "Cronus";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.DARK_PURPLE;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/cleave" Command:
	private static final String CLEAVE_NAME = "Cleave"; // Sets the name of this command
	private static final int CLEAVE_COST = 100; // Cost to run command in "favor"
	private static final int CLEAVE_DELAY = 1000; // In milliseconds

	// "/slow" Command:
	private static final String SLOW_NAME = "Slow"; // Sets the name of this command
	private static final int SLOW_COST = 180; // Cost to run command in "favor"
	private static final int SLOW_DELAY = 1000; // In milliseconds

	// "/timestop" Command:
	@SuppressWarnings("unused")
	private static String ULTIMATE_NAME = "Timestop";
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	@Override
	public List<Material> getClaimItems()
	{
		List<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.SOUL_SAND);
		// claimItems.add(Material.WATCH);
		claimItems.add(Material.DIRT);

		return claimItems;
	}

	@Override
	public List<String> getInfo(Player player)
	{
		List<String> toReturn = new ArrayList<String>();

		if(MiscAPI.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/cleave" + ChatColor.WHITE + " - Do damage to your target.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/slow" + ChatColor.WHITE + " - Slow your target.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/timestop" + ChatColor.WHITE + " - Stop time for your enemies while you plan your next attack.");
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
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/cleave" + ChatColor.WHITE + " - Do damage to your target.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/slow" + ChatColor.WHITE + " - Slow your target.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/timestop" + ChatColor.WHITE + " - Stop time for our enemies while you plan your next attack.");
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

	// This sets the particular passive ability for the Cronus deity.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageByEntityEvent damageEvent)
	{
		if(damageEvent.getDamager() instanceof Player)
		{
			Player player = (Player) damageEvent.getDamager();
			PlayerCharacter character = PlayerAPI.getCurrentChar(player);

			if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

			if(!ZoneAPI.canTarget(damageEvent.getEntity())) return;

			if(!player.getItemInHand().getType().name().contains("_HOE")) return;

			if(damageEvent.getEntity() instanceof Player)
			{
				Player attacked = (Player) damageEvent.getEntity();

				// Cronus Passive: Stop movement
				if(!PlayerAPI.areAllied(player, attacked)) attacked.setVelocity(new Vector(0, 0, 0));
			}

			if(character.getAbilities().isEnabledAbility(CLEAVE_NAME))
			{
				if(!CharacterAPI.isCooledDown(player, CLEAVE_NAME, false)) return;

				cleave(damageEvent);
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

		if(character.getAbilities().isEnabledAbility(SLOW_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBindings().getBind(SLOW_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, SLOW_NAME, false)) return;

			slow(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/cleave"
	 */
	public static void cleaveCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(character.getAbilities().isEnabledAbility(CLEAVE_NAME))
		{
			character.getAbilities().toggleAbility(CLEAVE_NAME, false);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is no longer active.");
		}
		else
		{
			character.getAbilities().toggleAbility(CLEAVE_NAME, true);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is now active.");
		}
	}

	// The actual ability command
	public static void cleave(EntityDamageByEntityEvent damageEvent)
	{
		// Define variables
		Player player = (Player) damageEvent.getDamager();
		Entity attacked = damageEvent.getEntity();
		if(!(attacked instanceof LivingEntity)) return;
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!AbilityAPI.doAbilityPreProcess(player, (LivingEntity) attacked, "cleave", CLEAVE_COST, AbilityType.OFFENSE)) return;
		CharacterAPI.setCoolDown(player, CLEAVE_NAME, System.currentTimeMillis() + CLEAVE_DELAY);
		character.subtractFavor(CLEAVE_COST);

		for(int i = 1; i <= 31; i += 4)
			attacked.getWorld().playEffect(attacked.getLocation(), Effect.SMOKE, i);

		MiscAPI.customDamage(player, (LivingEntity) attacked, (int) Math.ceil(Math.pow(character.getPower(AbilityType.OFFENSE), 0.35)), DamageCause.ENTITY_ATTACK);

		if((LivingEntity) attacked instanceof Player)
		{
			Player attackedPlayer = (Player) attacked;

			attackedPlayer.setFoodLevel(attackedPlayer.getFoodLevel() - (damageEvent.getDamage() / 2));

			if(attackedPlayer.getFoodLevel() < 0) attackedPlayer.setFoodLevel(0);
		}
	}

	/*
	 * Command: "/slow"
	 */
	public static void slowCommand(Player player, String[] args)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.getBindings().setBound(SLOW_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.getAbilities().isEnabledAbility(SLOW_NAME))
			{
				character.getAbilities().toggleAbility(SLOW_NAME, false);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is no longer active.");
			}
			else
			{
				character.getAbilities().toggleAbility(SLOW_NAME, true);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void slow(Player player)
	{
		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);
		int power = character.getDevotion();
		int duration = (int) Math.ceil(3.635 * Math.pow(power, 0.2576)); // seconds
		int strength = (int) Math.ceil(1.757 * Math.pow(power, 0.097));
		Player target = null;
		if(AbilityAPI.autoTarget(player) instanceof Player) target = (Player) AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "slow", SLOW_COST, AbilityType.SUPPORT)) return;
		CharacterAPI.setCoolDown(player, SLOW_NAME, System.currentTimeMillis() + SLOW_DELAY);
		character.subtractFavor(SLOW_COST);

		if(!AbilityAPI.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, strength));
			player.sendMessage(ChatColor.YELLOW + PlayerAPI.getCurrentChar(target).getName() + " has been slowed.");
			target.sendMessage(ChatColor.RED + "You have been slowed for " + duration + " seconds.");
		}
	}

	/*
	 * Command: "/timestop"
	 */
	public static void timestopCommand(Player player, String[] args)
	{
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "" + DEITYNAME + ".ultimate")) return;

		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		// Check the player for DEITYNAME
		if(!character.isDeity(DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(CharacterAPI.isCooledDown(player, ULTIMATE_NAME, false))
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + ((((CharacterAPI.getCoolDown(player, ULTIMATE_NAME)) / 1000) - (System.currentTimeMillis() / 1000))) / 60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((CharacterAPI.getCoolDown(player, ULTIMATE_NAME)) / 1000) - (System.currentTimeMillis() / 1000)) % 60) + " seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(!AbilityAPI.doAbilityPreProcess(player, "timestop", ULTIMATE_COST, AbilityType.SUPPORT)) return;

		int duration = (int) Math.round(9.9155621 * Math.pow(character.getAscensions(), 0.459019));
		player.sendMessage(ChatColor.YELLOW + "Cronus has stopped time for " + duration + " seconds, for " + timestop(player, duration) + " enemies!");

		// Set favor and cooldown
		character.subtractFavor(ULTIMATE_COST);
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		CharacterAPI.setCoolDown(player, ULTIMATE_NAME, System.currentTimeMillis() + cooldownMultiplier * 1000);
	}

	// The actual ability command
	public static int timestop(Player player, int duration)
	{
		// Define variables
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		int slowamount = (int) Math.round(4.77179 * Math.pow(character.getAscensions(), 0.17654391));
		int count = 0;

		for(Player onlinePlayer : player.getWorld().getPlayers())
		{
			if(!(onlinePlayer.getLocation().toVector().isInSphere(player.getLocation().toVector(), 70))) continue;

			if(!ZoneAPI.canTarget(onlinePlayer)) continue;

			if(PlayerAPI.areAllied(player, onlinePlayer)) continue;

			onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, slowamount));

			count++;
		}

		return count;
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
		List<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("cleave");
		COMMANDS.add("slow");
		COMMANDS.add("timestop");

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
