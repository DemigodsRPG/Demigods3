package com.censoredsoftware.demigods.greek.deity.titan;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.offense.Blaze;
import com.censoredsoftware.demigods.greek.ability.offense.Fireball;
import com.censoredsoftware.demigods.greek.ability.passive.NoFire;
import com.censoredsoftware.demigods.greek.ability.ultimate.Firestorm;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Perses extends GreekDeity
{
	public final static String name = "Perses", shortDescription = ChatColor.GRAY + "The Titan of anger and destruction.";
	public final static Alliance alliance = GreekAlliance.TITAN;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 30, favorBank = 10000;
	public final static ChatColor color = ChatColor.GOLD;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.FLINT, 1));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.FIREWORK, 4));
	public final static List<String> lore = Arrays.asList();
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList(new Fireball(name, permission), new Blaze(name, permission), new Firestorm(name, permission), new NoFire(name, permission));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Perses()
	{
		super(name, permission, alliance, color, claimItems, forsakeItems, shortDescription, lore, flags, abilities, accuracy, favorRegen, maxFavor, maxHealth, favorBank, moodPacks);
	}

	private static final Deity INST = new Perses();

	public static Deity inst()
	{
		return INST;
	}
}
