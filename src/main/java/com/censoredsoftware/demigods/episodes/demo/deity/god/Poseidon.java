package com.censoredsoftware.demigods.episodes.demo.deity.god;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.util.Unicodes;
import com.censoredsoftware.demigods.episodes.demo.ability.offense.Reel;
import com.censoredsoftware.demigods.episodes.demo.ability.passive.InfiniteAir;
import com.censoredsoftware.demigods.episodes.demo.ability.passive.Swim;

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
				add(ChatColor.GRAY + " " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private final static Type type = Type.DEMO;
	private final static Set<Ability> abilities = new HashSet<Ability>(3)
	{
		{
			add(new Swim(name, permission));
			add(new Reel(name, permission));
			add(new InfiniteAir(name, permission));
		}
	};

	public Poseidon()
	{
		super(new Info(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}
