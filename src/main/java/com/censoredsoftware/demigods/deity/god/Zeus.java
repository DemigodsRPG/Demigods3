package com.censoredsoftware.demigods.deity.god;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.offense.Shove;
import com.censoredsoftware.demigods.ability.passive.NoFall;
import com.censoredsoftware.demigods.ability.ultimate.Storm;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.util.Unicodes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Zeus implements Deity
{
	private final static String name = "Zeus", alliance = "God", permission = "demigods.god.zeus";
	private final static ChatColor color = ChatColor.YELLOW;
	private final static Set<Material> claimItems = Sets.newHashSet(Material.FEATHER);
	private final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.FEATHER, 10));
	private final static List<String> lore = new ArrayList<String>(5 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.RED + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(" ");
			add(ChatColor.YELLOW + " Claim Items:");
			add(" ");
			for(Material item : claimItems)
				add(ChatColor.GRAY + "   " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			add(" ");
			add(ChatColor.YELLOW + " Abilities:");
			add(" ");
		}
	};
	private final static Type type = Type.TIER1;
	private final static Set<Ability> abilities = Sets.newHashSet(new NoFall(name, permission), new Shove(name, permission), new Storm.Lightning(name, permission), new Storm(name, permission));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Elements.ListedDeity getListedDeity()
	{
		return Elements.Deities.ZEUS;
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
