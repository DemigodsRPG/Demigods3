package com.censoredsoftware.Demigods.Deity.Titan;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.API.*;
import com.censoredsoftware.Demigods.Deity.Deity;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Event.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

public class Prometheus_deity implements Deity, Listener
{
	// Create required universal deity variables
	private static final String DEITYNAME = "Prometheus";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.GOLD;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/fireball" Command:
	private static final String FIREBALL_NAME = "Fireball"; // Sets the name of this command
	private static long FIREBALL_TIME; // Creates the variable for later use
	private static final int FIREBALL_COST = 100; // Cost to run command in "favor"
	private static final int FIREBALL_DELAY = 5; // In milliseconds

	// "/blaze" Command:
	private static final String BLAZE_NAME = "Blaze"; // Sets the name of this command
	private static long BLAZE_TIME; // Creates the variable for later use
	private static final int BLAZE_COST = 400; // Cost to run command in "favor"
	private static final int BLAZE_DELAY = 15; // In milliseconds

	// "/firestorm" Command:
	@SuppressWarnings("unused")
	private static String ULTIMATE_NAME = "Firestorm";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 5500; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.CLAY_BALL);
		// claimItems.add(Material.MAGMA_CREAM);
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
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/fireball" + ChatColor.WHITE + " - Shoot a fireball at the cursor's location.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/blaze" + ChatColor.WHITE + " - Ignite the ground at the target location.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/firestorm" + ChatColor.WHITE + " - Prometheus rains fire on nearby enemies.");
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
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/fireball" + ChatColor.WHITE + " - Shoot a fireball at the cursor's location.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/blaze" + ChatColor.WHITE + " - Ignite the ground at the target location.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/firestorm" + ChatColor.WHITE + " - Prometheus rains fire on nearby enemies.");
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);

		if(!AbilityAPI.isClick(interactEvent)) return;

		if(!MiscAPI.canUseDeitySilent(player, DEITYNAME)) return;

		if(character.isEnabledAbility(FIREBALL_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBind(FIREBALL_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, FIREBALL_NAME, FIREBALL_TIME, false)) return;

			fireball(player);
		}
		else if(character.isEnabledAbility(BLAZE_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBind(BLAZE_NAME))))
		{
			if(!CharacterAPI.isCooledDown(player, BLAZE_NAME, BLAZE_TIME, false)) return;

			blaze(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/fireball"
	 */
	public static void fireballCommand(Player player, String[] args)
	{
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.setBound(FIREBALL_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.isEnabledAbility(FIREBALL_NAME))
			{
				character.toggleAbility(FIREBALL_NAME, false);
				player.sendMessage(ChatColor.YELLOW + FIREBALL_NAME + " is no longer active.");
			}
			else
			{
				character.toggleAbility(FIREBALL_NAME, true);
				player.sendMessage(ChatColor.YELLOW + FIREBALL_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void fireball(Player player)
	{
		// Define variables
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);
		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "fireball", BLAZE_COST, AbilityType.OFFENSE)) return;
		FIREBALL_TIME = System.currentTimeMillis() + FIREBALL_DELAY;
		character.subtractFavor(FIREBALL_COST);

		if(!AbilityAPI.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			shootFireball(player.getEyeLocation(), target.getLocation(), player);
		}
	}

	public static void shootFireball(Location from, Location to, Player player)
	{
		player.getWorld().spawnEntity(from, EntityType.FIREBALL);
		for(Entity entity : player.getNearbyEntities(2, 2, 2))
		{
			if(!(entity instanceof Fireball)) continue;

			Fireball fireball = (Fireball) entity;
			to.setX(to.getX() + .5);
			to.setY(to.getY() + .5);
			to.setZ(to.getZ() + .5);
			Vector path = to.toVector().subtract(from.toVector());
			Vector victor = from.toVector().add(from.getDirection().multiply(2));
			fireball.teleport(new Location(player.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
			fireball.setDirection(path);
			fireball.setShooter(player);
		}
	}

	/*
	 * Command: "/blaze"
	 */
	public static void blazeCommand(Player player, String[] args)
	{
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);

		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!MiscAPI.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.setBound(BLAZE_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.isEnabledAbility(BLAZE_NAME))
			{
				character.toggleAbility(BLAZE_NAME, false);
				player.sendMessage(ChatColor.YELLOW + BLAZE_NAME + " is no longer active.");
			}
			else
			{
				character.toggleAbility(BLAZE_NAME, true);
				player.sendMessage(ChatColor.YELLOW + BLAZE_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void blaze(Player player)
	{
		// Define variables
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);
		int power = character.getPower(AbilityType.OFFENSE);
		int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
		if(diameter > 12) diameter = 12;

		LivingEntity target = AbilityAPI.autoTarget(player);

		if(!AbilityAPI.doAbilityPreProcess(player, target, "blaze", BLAZE_COST, AbilityType.OFFENSE)) return;
		BLAZE_TIME = System.currentTimeMillis() + BLAZE_DELAY;
		character.subtractFavor(BLAZE_COST);

		if(!AbilityAPI.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			for(int X = -diameter / 2; X <= diameter / 2; X++)
			{
				for(int Y = -diameter / 2; Y <= diameter / 2; Y++)
				{
					for(int Z = -diameter / 2; Z <= diameter / 2; Z++)
					{
						Block block = target.getWorld().getBlockAt(target.getLocation().getBlockX() + X, target.getLocation().getBlockY() + Y, target.getLocation().getBlockZ() + Z);
						if((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !ZoneAPI.zoneNoBuild(player, block.getLocation()))) block.setType(Material.FIRE);
					}
				}
			}
		}
	}

	/*
	 * Command: "/firestorm"
	 */
	public static void firestormCommand(Player player, String[] args)
	{
		if(!Demigods.permission.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")) return;

		// Define variables
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);

		// Check the player for DEITYNAME
		if(!character.isClass(DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + ((((ULTIMATE_TIME) / 1000) - (System.currentTimeMillis() / 1000))) / 60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((ULTIMATE_TIME) / 1000) - (System.currentTimeMillis() / 1000)) % 60) + " seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(!AbilityAPI.doAbilityPreProcess(player, "firestorm", ULTIMATE_COST, AbilityType.OFFENSE)) return;

		firestorm(player);
		player.sendMessage(ChatColor.YELLOW + "Prometheus has reigned fire down on your enemies.");

		// Set favor and cooldown
		character.subtractFavor(ULTIMATE_COST);
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
	}

	// The actual ability command
	public static void firestorm(final Player player)
	{
		// Define variables
		PlayerCharacterClass character = PlayerAPI.getCurrentChar(player);
		int power = character.getPower(AbilityType.OFFENSE);
		int total = 20 * (int) Math.round(2 * Math.pow(power, 0.15));
		Vector playerLocation = player.getLocation().toVector();
		final ArrayList<LivingEntity> entityList = new ArrayList<LivingEntity>();
		for(Entity entity : player.getNearbyEntities(50, 50, 50))
		{
			if(!(entity instanceof LivingEntity)) continue;
			if(entity instanceof Player) if(PlayerAPI.getCurrentChar((Player) entity) != null) if(PlayerAPI.areAllied(player, (Player) entity)) continue;
			if(!ZoneAPI.canTarget(entity)) continue;
			entityList.add((LivingEntity) entity);
		}
		for(int i = 0; i <= total; i += 20)
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.demigods, new Runnable()
			{
				@Override
				public void run()
				{
					for(LivingEntity entity : entityList)
					{
						Location up = new Location(entity.getWorld(), entity.getLocation().getX() + Math.random() * 5, 256, entity.getLocation().getZ() + Math.random() * 5);
						up.setPitch(90);
						shootFireball(up, new Location(entity.getWorld(), entity.getLocation().getX() + Math.random() * 5, entity.getLocation().getY(), entity.getLocation().getZ() + Math.random() * 5), player);
					}
				}
			}, i);
		}
	}

	@Override
	public String loadDeity()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Demigods.demigods);
		ULTIMATE_TIME = System.currentTimeMillis();
		FIREBALL_TIME = System.currentTimeMillis();
		BLAZE_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}

	@Override
	public List<String> getCommands()
	{
		ArrayList<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("fireball");
		COMMANDS.add("blaze");
		COMMANDS.add("firestorm");

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
