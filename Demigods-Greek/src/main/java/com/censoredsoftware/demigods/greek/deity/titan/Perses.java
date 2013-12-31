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
	public final static String NAME = "Perses", SHORT_DESCRIPTION = ChatColor.GRAY + "The Titan of anger and destruction.";
	public final static Alliance ALLIANCE = GreekAlliance.TITAN;
	public final static String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public final static int ACCURACU = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 30, FAVOR_BANK = 10000;
	public final static ChatColor COLOR = ChatColor.GOLD;
	public final static Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FLINT, 1));
	public final static Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FIREWORK, 4));
	public final static List<String> LORE = Arrays.asList();
	public final static Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public final static List<Ability> ABILITIES = Lists.newArrayList(new Fireball(NAME), new Blaze(NAME), new Firestorm(NAME), new NoFire(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Perses()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACU, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Perses();

	public static Deity inst()
	{
		return INST;
	}
}
