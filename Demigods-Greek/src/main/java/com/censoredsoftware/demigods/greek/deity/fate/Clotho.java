package com.censoredsoftware.demigods.greek.deity.fate;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Strings;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.AlwaysInvisible;
import com.censoredsoftware.demigods.greek.ability.passive.NoDamage;
import com.censoredsoftware.demigods.greek.ability.passive.Swim;
import com.censoredsoftware.demigods.greek.ability.support.Carry;
import com.censoredsoftware.demigods.greek.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public class Clotho extends GreekDeity
{
	private static final String name = "Clotho", shortDescription = ChatColor.GRAY + "The one who sows.";
	private static final Alliance alliance = GreekAlliance.FATE;
	private static final String permission = alliance.getPermission() + "." + name.toLowerCase();
	private static final int accuracy = 15, favorRegen = 999, maxFavor = 20000, favorBank = 10000;
	private static final double maxHealth = 2;
	private static final ChatColor color = ChatColor.WHITE;
	private static final ImmutableMap<Material, Integer> claimItems = ImmutableMap.of(Material.BEDROCK, 3);
	private static final ImmutableMap<Material, Integer> forsakeItems = ImmutableMap.of(Material.BEDROCK, 10);
	private static final ImmutableList<String> lore = ImmutableList.copyOf(new ArrayList<String>(9 + claimItems.size())
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
	});
	private static final Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
	private static final List<Ability> abilities = Lists.newArrayList(new NoDamage(name, permission), new AlwaysInvisible(name, permission), new Swim(name, permission), new Carry(name, permission, false), new Discoball(name, permission));

	// Mood Manager
	private static EnumMap<Deity.Mood, Deity.MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Clotho()
	{
		super(name, permission, alliance, color, claimItems, forsakeItems, shortDescription, lore, flags, abilities, accuracy, favorRegen, maxFavor, maxHealth, favorBank, moodPacks);
	}

	private static final Deity INST = new Clotho();

	public static Deity inst()
	{
		return INST;
	}
}
