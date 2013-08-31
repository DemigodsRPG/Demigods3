package com.censoredsoftware.demigods.deity.donor;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.passive.NoSplosion;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.util.Strings;
import com.censoredsoftware.demigods.util.Unicodes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OmegaX17 implements Deity
{
	private final static String name = "OmegaX17", alliance = "Donor", permission = "demigods.donor.omega";
	private final static int accuracy = 15;
	private final static ChatColor color = ChatColor.BLACK;
	private final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.TNT, 3));
	private final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.FLINT_AND_STEEL, 6));
	private final static List<String> lore = new ArrayList<String>(9 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.RED + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(" ");
			add(ChatColor.YELLOW + " Claim Items:");
			add(" ");
			for(Map.Entry<Material, Integer> entry : claimItems.entrySet())
				add(ChatColor.GRAY + " " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + entry.getValue() + " " + Strings.beautify(entry.getKey().name()).toLowerCase() + (entry.getValue() > 1 ? "s" : ""));
			add(" ");
			add(ChatColor.YELLOW + " Abilities:");
			add(" ");
		}
	};
	private final static Set<Flag> flags = Sets.newHashSet(Flag.MAJOR_DEITY, Flag.PLAYABLE);
	private final static Set<Ability> abilities = Sets.newHashSet((Ability) new NoSplosion(name, permission));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Demigods.ListedDeity getListedDeity()
	{
		return null; // Demigods..Deities.OMEGA;
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
	public Map<Material, Integer> getClaimItems()
	{
		return claimItems;
	}

	@Override
	public Map<Material, Integer> getForsakeItems()
	{
		return forsakeItems;
	}

	@Override
	public List<String> getLore()
	{
		return lore;
	}

	@Override
	public Set<Flag> getFlags()
	{
		return flags;
	}

	@Override
	public Set<Ability> getAbilities()
	{
		return abilities;
	}

	@Override
	public int getAccuracy()
	{
		return accuracy;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
