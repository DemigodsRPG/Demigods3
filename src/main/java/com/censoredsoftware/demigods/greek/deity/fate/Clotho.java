package com.censoredsoftware.demigods.greek.deity.fate;

import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.AlwaysInvisible;
import com.censoredsoftware.demigods.greek.ability.passive.NoDamage;
import com.censoredsoftware.demigods.greek.ability.passive.Swim;
import com.censoredsoftware.demigods.greek.ability.support.Carry;
import com.censoredsoftware.demigods.greek.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.language.Symbol;
import com.censoredsoftware.demigods.util.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clotho
{
	public final static String name = "Clotho", shortDescription = ChatColor.GRAY + "The one who sows.";
	public final static Alliance alliance = GreekAlliance.FATE;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 999, maxFavor = 20000, maxHealth = 2;
	public final static ChatColor color = ChatColor.WHITE;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 3));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 10));
	public final static List<String> lore = new ArrayList<String>(9 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.RED + " Demigods > " + ChatColor.RESET + color + name);
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
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
	public final static List<Ability> abilities = Lists.newArrayList(new NoDamage(name, permission), new AlwaysInvisible(name, permission), new Swim(name, permission), new Carry(name, permission, false), new Discoball(name, permission));
}
