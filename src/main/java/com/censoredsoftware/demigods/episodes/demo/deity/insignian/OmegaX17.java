package com.censoredsoftware.demigods.episodes.demo.deity.insignian;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.util.Unicodes;
import com.censoredsoftware.demigods.episodes.demo.ability.passive.NoSplosion;

public class OmegaX17 extends Deity
{
	private final static String name = "OmegaX17", alliance = "Insignian", permission = "demigods.insignian.omega";
	private final static ChatColor color = ChatColor.BLACK;
	private final static Set<Material> claimItems = new HashSet<Material>(1)
	{
		{
			add(Material.TNT);
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
	private final static Set<Ability> abilities = new HashSet<Ability>(1)
	{
		{
			add(new NoSplosion(name, permission));
		}
	};

	public OmegaX17()
	{
		super(new Info(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}
