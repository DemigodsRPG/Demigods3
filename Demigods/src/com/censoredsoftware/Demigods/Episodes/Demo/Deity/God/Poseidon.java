package com.censoredsoftware.Demigods.Episodes.Demo.Deity.God;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;

public class Poseidon extends Deity
{
	private static String name = "Poseidon", alliance = "God";
	private static ChatColor color = ChatColor.AQUA;
	private static Set<Material> claimItems = new HashSet<Material>()
	{
		{
			add(Material.DIRT);
		}
	};
	private static List<String> lore = new ArrayList<String>()
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
	private static Type type = Type.DEMO;
	private static Set<Ability> abilities = new HashSet<Ability>()
	{
		{
			add(new Swim());
		}
	};

	public Poseidon()
	{
		super(new DeityInfo(name, alliance, color, claimItems, lore, type), abilities);
	}
}

class Swim extends Ability
{
	private static String deity = "Poseidon", name = "Swim", command = null, permission = "demigods.god.poseidon";
	private static int cost = 0, delay = 0, cooldownMin = 0, cooldownMax = 0;
	private static AbilityInfo info;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + "Crouch while in water to swim like Poseidon.");
		}
	};
	private static Devotion.Type type = Devotion.Type.PASSIVE;

	protected Swim()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), new Listener()
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
		});
	}
}
