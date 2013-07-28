package com.censoredsoftware.why.episodes.demo.deity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.why.engine.element.Ability;
import com.censoredsoftware.why.engine.element.Deity;
import com.censoredsoftware.why.engine.util.UnicodeUtility;
import com.censoredsoftware.why.episodes.demo.ability.Template;

public class TemplateDeity extends Deity
{
	private final static String name = "Template", alliance = "Test", permission = "demigods.test.test";
	private final static ChatColor color = ChatColor.GRAY;
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
	private final static Set<Ability> abilities = new HashSet<Ability>(1)
	{
		{
			add(new Template(name, permission));
		}
	};

	public TemplateDeity()
	{
		super(new Info(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}
