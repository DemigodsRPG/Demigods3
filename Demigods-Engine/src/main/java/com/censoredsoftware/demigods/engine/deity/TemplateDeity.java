package com.censoredsoftware.demigods.engine.deity;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TemplateDeity
{
	public final static String name = "Template", shortDescription = ChatColor.GRAY + "The deity of testing.";
	public final static Alliance alliance = new Alliance()
	{
		@Override
		public String getName()
		{
			return "Test";
		}

		@Override
		public String getShortDescription()
		{
			return "Test alliance.";
		}

		@Override
		public String getPermission()
		{
			return "demigods.alliance.test";
		}

		@Override
		public PermissionDefault getPermissionDefault()
		{
			return PermissionDefault.FALSE;
		}

		@Override
		public boolean isPlayable()
		{
			return false;
		}
	};
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 40;
	public final static ChatColor color = ChatColor.GRAY;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 1));
	public final static List<String> lore = new ArrayList<String>(9 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(" ");
			add(ChatColor.YELLOW + " Claim Items:");
			add(" ");
			for(Map.Entry<Material, Integer> entry : claimItems.entrySet())
				add(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.WHITE + entry.getValue() + " " + Strings.beautify(entry.getKey().name()).toLowerCase() + (entry.getValue() > 1 ? "s" : ""));
			add(" ");
			add(ChatColor.YELLOW + " Abilities:");
			add(" ");
		}
	};
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.NON_PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList((Ability) new TemplateAbility(name, permission));
}
