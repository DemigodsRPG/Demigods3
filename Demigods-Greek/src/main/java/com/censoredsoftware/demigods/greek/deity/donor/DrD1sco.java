package com.censoredsoftware.demigods.greek.deity.donor;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.RainbowHorse;
import com.censoredsoftware.demigods.greek.ability.passive.RainbowWalking;
import com.censoredsoftware.demigods.greek.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DrD1sco extends GreekDeity
{
	public static final String NAME = "DrD1sco", SHORT_DESCRIPTION = ChatColor.BLUE + "The donor of dance.";
	public static final Alliance ALLIANCE = GreekAlliance.DONOR;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 40, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.DARK_PURPLE;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.JUKEBOX, 2));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.NOTE_BLOCK, 4));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.NON_PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList(new RainbowWalking(NAME), new RainbowHorse(NAME), new Discoball(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private DrD1sco()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new DrD1sco();

	public static Deity inst()
	{
		return INST;
	}
}
