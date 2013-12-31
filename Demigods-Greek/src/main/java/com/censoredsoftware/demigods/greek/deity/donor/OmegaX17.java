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
	public final static String NAME = "OmegaX17", SHORT_DESCRIPTION = ChatColor.RED + "The donor of explosions.";
	public final static Alliance ALLIANCE = GreekAlliance.DONOR;
	public final static String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public final static int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 40, FAVOR_BANK = 10000;
	public final static ChatColor COLOR = ChatColor.BLACK;
	public final static Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.TNT, 3));
	public final static Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FLINT_AND_STEEL, 6));
	public final static List<String> LORE = Arrays.asList();
	public final static Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.NON_PLAYABLE);
	public final static List<Ability> ABILITIES = Lists.newArrayList((Ability) new NoSplosion(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private OmegaX17()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new OmegaX17();

	public static Deity inst()
	{
		return INST;
	}
}
