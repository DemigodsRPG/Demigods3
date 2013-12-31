package com.censoredsoftware.demigods.greek.deity.donor;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.NoSplosion;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class OmegaX17 extends GreekDeity
{
	public final static String name = "OmegaX17", shortDescription = ChatColor.RED + "The donor of explosions.";
	public final static Alliance alliance = GreekAlliance.DONOR;
	public final static String permission = alliance.getPermission() + "." + name.toLowerCase();
	public final static int accuracy = 15, favorRegen = 5, maxFavor = 20000, maxHealth = 40, favorBank = 10000;
	public final static ChatColor color = ChatColor.BLACK;
	public final static Map<Material, Integer> claimItems = Maps.newHashMap(ImmutableMap.of(Material.TNT, 3));
	public final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.FLINT_AND_STEEL, 6));
	public final static List<String> lore = Arrays.asList();
	public final static Set<Deity.Flag> flags = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.NON_PLAYABLE);
	public final static List<Ability> abilities = Lists.newArrayList((Ability) new NoSplosion(name, permission));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private OmegaX17()
	{
		super(name, permission, alliance, color, claimItems, forsakeItems, shortDescription, lore, flags, abilities, accuracy, favorRegen, maxFavor, maxHealth, favorBank, moodPacks);
	}

	private static final Deity INST = new OmegaX17();

	public static Deity inst()
	{
		return INST;
	}
}
