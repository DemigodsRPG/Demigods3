package com.censoredsoftware.demigods.greek.deity.god;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.offense.Reel;
import com.censoredsoftware.demigods.greek.ability.passive.Carry;
import com.censoredsoftware.demigods.greek.ability.passive.NoDrown;
import com.censoredsoftware.demigods.greek.ability.passive.Swim;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public class Poseidon extends GreekDeity
{
	public static final String NAME = "Poseidon", SHORT_DESCRIPTION = ChatColor.GRAY + "The God of the oceans.";
	public static final Alliance ALLIANCE = GreekAlliance.GOD;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 50, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.AQUA;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.INK_SACK, 4));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.WATER_BUCKET, 4));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList(new Swim(NAME), new NoDrown(NAME), new Reel(NAME), new Carry(NAME, true));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Poseidon()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Poseidon();

	public static Deity inst()
	{
		return INST;
	}
}
