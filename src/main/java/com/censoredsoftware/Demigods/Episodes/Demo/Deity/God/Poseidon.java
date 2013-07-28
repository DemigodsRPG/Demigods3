package com.censoredsoftware.Demigods.Episodes.Demo.Deity.God;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.Demigods.Engine.Element.Ability;
import com.censoredsoftware.Demigods.Engine.Element.Deity;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;
import com.censoredsoftware.Demigods.Episodes.Demo.Ability.Offense.Reel;
import com.censoredsoftware.Demigods.Episodes.Demo.Ability.Passive.InfiniteAir;
import com.censoredsoftware.Demigods.Episodes.Demo.Ability.Passive.Swim;

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
