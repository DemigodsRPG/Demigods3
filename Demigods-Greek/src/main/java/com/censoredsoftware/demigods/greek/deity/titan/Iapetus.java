package com.censoredsoftware.demigods.greek.deity.titan;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Iapetus extends GreekDeity
{
	public final static String name = "Iapetus", shortDescription = ChatColor.GRAY + "The Titan of mortality.";
	public final static Alliance alliance = GreekAlliance.TITAN;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 40, favorBank = 10000;
	public final static ChatColor color = ChatColor.RED;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.ARROW, 2, Material.WOOD_SWORD, 1));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.GOLD_SWORD, 4));
	public final static List<String> lore = Arrays.asList();
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.NON_PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList();

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Iapetus()
	{
		super(name, permission, alliance, color, claimItems, forsakeItems, shortDescription, lore, flags, abilities, accuracy, favorRegen, maxFavor, maxHealth, favorBank, moodPacks);
	}

	private static final Deity INST = new Iapetus();

	public static Deity inst()
	{
		return INST;
	}
}
