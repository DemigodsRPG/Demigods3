package com.censoredsoftware.demigods.deity;

import com.censoredsoftware.demigods.Element;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.Template;
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

public class TemplateDeity implements Deity
{
	private final static String name = "Template", alliance = "Test", permission = "demigods.test.template";
	private final static int accuracy = 15;
	private final static ChatColor color = ChatColor.GRAY;
	private final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	private final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	private final static List<String> lore = new ArrayList<String>(9 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
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
	private final static Set<Flag> flags = Sets.newHashSet(Flag.NON_PLAYABLE);
	private final static Set<Ability> abilities = Sets.newHashSet((Ability) new Template(name, permission));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Element.ListedDeity getListedDeity()
	{
		return null;
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
