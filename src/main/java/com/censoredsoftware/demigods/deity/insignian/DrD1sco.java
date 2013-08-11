package com.censoredsoftware.demigods.deity.insignian;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.core.util.Unicodes;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.passive.RainbowHorse;
import com.censoredsoftware.demigods.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.deity.Deity;

public class DrD1sco extends Deity
{
	private final static String name = "DrD1sco", alliance = "Insignian", permission = "demigods.insignian.disco";
	private final static ChatColor color = ChatColor.DARK_PURPLE;
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
	private final static Set<Ability> abilities = new HashSet<Ability>(2)
	{
		{
			add(new Discoball.RainbowWalking(name, permission));
			add(new RainbowHorse(name, permission));
		}
	};

	public DrD1sco()
	{
		super(name, alliance, permission, color, claimItems, lore, type, abilities);
	}

}
