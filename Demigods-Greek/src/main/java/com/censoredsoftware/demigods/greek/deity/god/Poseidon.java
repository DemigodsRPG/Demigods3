package com.censoredsoftware.demigods.greek.deity.god;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.offense.Reel;
import com.censoredsoftware.demigods.greek.ability.passive.NoDrown;
import com.censoredsoftware.demigods.greek.ability.passive.Swim;
import com.censoredsoftware.demigods.greek.ability.support.Carry;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Poseidon extends GreekDeity
{
	public final static String name = "Poseidon", shortDescription = ChatColor.GRAY + "The God of the oceans.";
	public final static Alliance alliance = GreekAlliance.GOD;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 50, favorBank = 10000;
	public final static ChatColor color = ChatColor.AQUA;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.INK_SACK, 4));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.WATER_BUCKET, 4));
	public final static List<String> lore = Arrays.asList();
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList(new Swim(name, permission), new NoDrown(name, permission), new Reel(name, permission), new Carry(name, permission, true));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Poseidon()
	{
		super(name, permission, alliance, color, claimItems, forsakeItems, shortDescription, lore, flags, abilities, accuracy, favorRegen, maxFavor, maxHealth, favorBank, moodPacks);
	}

	private static final Deity INST = new Poseidon();

	public static Deity inst()
	{
		return INST;
	}
}
