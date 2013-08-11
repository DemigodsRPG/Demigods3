package com.censoredsoftware.demigods.deity.titan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.core.util.Unicodes;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.offense.Blaze;
import com.censoredsoftware.demigods.ability.ultimate.Firestorm;
import com.censoredsoftware.demigods.deity.Deity;

public class Prometheus extends Deity
{
	private final static String name = "Prometheus", alliance = "Titan", permission = "demigods.titan.protmetheus";
	private final static ChatColor color = ChatColor.GOLD;
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
				add(ChatColor.GRAY + " " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private final static Type type = Type.DEMO;
	private final static Set<Ability> abilities = new HashSet<Ability>(3)
	{
		{
			add(new Firestorm.ShootFireball(name, permission));
			add(new Blaze(name, permission));
			add(new Firestorm(name, permission));
		}
	};

	public Prometheus()
	{
		super(name, alliance, permission, color, claimItems, lore, type, abilities);
	}
}
