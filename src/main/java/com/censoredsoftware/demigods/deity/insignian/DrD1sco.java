package com.censoredsoftware.demigods.deity.insignian;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.core.util.Unicodes;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.passive.RainbowHorse;
import com.censoredsoftware.demigods.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.deity.Deity;
import com.google.common.collect.Sets;

public class DrD1sco implements Deity
{
	private final static String name = "DrD1sco", alliance = "Insignian", permission = "demigods.insignian.disco";
	private final static ChatColor color = ChatColor.DARK_PURPLE;
	private final static Set<Material> claimItems = Sets.newHashSet(Material.JUKEBOX);
	private final static List<String> lore = new ArrayList<String>(5 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : claimItems)
				add(ChatColor.GRAY + " " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private final static Type type = Type.TIER1;
	private final static Set<Ability> abilities = Sets.newHashSet(new Discoball.RainbowWalking(name, permission), new RainbowHorse(name, permission));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getAlliance()
	{
		return alliance;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public ChatColor getColor()
	{
		return color;
	}

	@Override
	public Set<Material> getClaimItems()
	{
		return claimItems;
	}

	@Override
	public List<String> getLore()
	{
		return lore;
	}

	@Override
	public Type getType()
	{
		return type;
	}

	@Override
	public Set<Ability> getAbilities()
	{
		return abilities;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
