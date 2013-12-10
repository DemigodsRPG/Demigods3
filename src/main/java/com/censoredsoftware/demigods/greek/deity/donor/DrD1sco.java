package com.censoredsoftware.demigods.greek.deity.donor;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Strings;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.RainbowHorse;
import com.censoredsoftware.demigods.greek.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
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

public class DrD1sco
{
	public final static String name = "DrD1sco", shortDescription = ChatColor.BLUE + "The donor of dance.";
	public final static Alliance alliance = GreekAlliance.DONOR;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 40;
	public final static ChatColor color = ChatColor.DARK_PURPLE;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.JUKEBOX, 2));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.NOTE_BLOCK, 4));
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
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList(new Discoball.RainbowWalking(name, permission), new RainbowHorse(name, permission), new Discoball(name, permission));
}
