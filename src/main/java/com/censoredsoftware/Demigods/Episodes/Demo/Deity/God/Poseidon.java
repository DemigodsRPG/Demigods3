package com.censoredsoftware.Demigods.Episodes.Demo.Deity.God;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;

public class Poseidon extends Deity
{
	private final static String name = "Poseidon", alliance = "God", permission = "demigods.god.poseidon";
	private final static ChatColor color = ChatColor.AQUA;
	private final static Set<Material> claimItems = new HashSet<Material>(1)
	{
		{
			add(Material.DIRT);
		}
	};
	private final static List<String> lore = new ArrayList<String>()
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : claimItems)
			{
				add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private final static Type type = Type.DEMO;
	private final static Set<Ability> abilities = new HashSet<Ability>(3)
	{
		{
			add(new Swim());
			add(new Reel());
			add(new InfiniteAir());
		}
	};

	public Poseidon()
	{
		super(new DeityInfo(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}

class Swim extends Ability
{
	private final static String deity = "Poseidon", name = "Swim", command = null, permission = "demigods.god.poseidon";
	private final static int cost = 0, delay = 0, repeat = 0;
	private static AbilityInfo info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Crouch while in water to swim like Poseidon.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.PASSIVE;

	protected Swim()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerMove(PlayerMoveEvent event)
			{
				Player player = event.getPlayer();
				if(!Deity.canUseDeitySilent(player, deity)) return;

				// PHELPS SWIMMING
				if(player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER))
				{
					Vector direction = player.getLocation().getDirection().normalize().multiply(1.3D);
					Vector victor = new Vector(direction.getX(), direction.getY(), direction.getZ());
					if(player.isSneaking()) player.setVelocity(victor);
				}
			}
		}, null);
	}
}

class Reel extends Ability
{
	private final static String deity = "Poseidon", name = "Reel", command = "reel", permission = "demigods.god.poseidon";
	private final static int cost = 120, delay = 1100, repeat = 0;
	private static AbilityInfo info;
	private final static Material weapon = Material.FISHING_ROD;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Use a fishing rod for a stronger attack.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.OFFENSE;

	protected Reel()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type, weapon), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				// Set variables
				Player player = interactEvent.getPlayer();
				PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();

				if(!Ability.isLeftClick(interactEvent)) return;

				if(!Deity.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isBound(name) && player.getItemInHand().getType() == weapon)
				{
					if(!PlayerCharacter.isCooledDown(character, name, false)) return;

					reel(player);
				}
			}
		}, null);
	}

	public static void reel(Player player)
	{
		// Set variables
		PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();
		int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
		LivingEntity target = Ability.autoTarget(player);

		if(!Ability.doAbilityPreProcess(player, target, name, cost, info)) return;
		character.getMeta().subtractFavor(cost);
		PlayerCharacter.setCoolDown(character, name, System.currentTimeMillis() + delay);

		if(!Ability.doTargeting(player, target.getLocation(), true)) return;

		Ability.dealDamage(player, target, damage, EntityDamageEvent.DamageCause.CUSTOM);

		if(target.getLocation().getBlock().getType() == Material.AIR)
		{
			target.getLocation().getBlock().setType(Material.WATER);
			target.getLocation().getBlock().setData((byte) 0x8);
		}
	}
}

class InfiniteAir extends Ability
{
	private final static String deity = "Poseidon", name = "InfiniteAir", command = null, permission = "demigods.god.poseidon";
	private final static int cost = 0, delay = 0, repeat = 0;
	private static AbilityInfo info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Have infinite air when moving underwater.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.PASSIVE;

	protected InfiniteAir()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerMove(PlayerMoveEvent event)
			{
				Player player = event.getPlayer();
				if(!Deity.canUseDeitySilent(player, deity)) return;

				if(player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER)) player.setRemainingAir(20);
			}
		}, null);
	}
}
