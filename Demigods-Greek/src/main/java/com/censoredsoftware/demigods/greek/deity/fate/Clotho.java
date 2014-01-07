package com.censoredsoftware.demigods.greek.deity.fate;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.AlwaysInvisible;
import com.censoredsoftware.demigods.greek.ability.passive.Carry;
import com.censoredsoftware.demigods.greek.ability.passive.NoDamage;
import com.censoredsoftware.demigods.greek.ability.passive.Swim;
import com.censoredsoftware.demigods.greek.ability.ultimate.Discoball;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Clotho extends GreekDeity
{
	private static final String NAME = "Clotho", SHORT_DESCRIPTION = ChatColor.GRAY + "The one who sows.";
	private static final Alliance ALLIANCE = GreekAlliance.FATE;
	private static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	private static final int ACCURACY = 15, FAVOR_REGEN = 999, MAX_FAVOR = 20000, FAVOR_BANK = 10000;
	private static final double MAX_HEALTH = 2;
	private static final ChatColor COLOR = ChatColor.WHITE;
	private static final ImmutableMap<Material, Integer> CLAIM_ITEMS = ImmutableMap.of(Material.BEDROCK, 3);
	private static final ImmutableMap<Material, Integer> FORSAKE_ITEMS = ImmutableMap.of(Material.BEDROCK, 10);
	public static final List<String> LORE = Arrays.asList();
	private static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
	private static final List<Ability> ABILITIES = Lists.newArrayList(new NoDamage(NAME), new AlwaysInvisible(NAME), new Swim(NAME), new Carry(NAME, false), new Discoball(NAME));

	// Mood Manager
	private static EnumMap<Deity.Mood, Deity.MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Clotho()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Clotho();

	public static Deity inst()
	{
		return INST;
	}
}
