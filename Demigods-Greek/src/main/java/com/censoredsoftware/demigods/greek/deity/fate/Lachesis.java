package com.censoredsoftware.demigods.greek.deity.fate;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.AlwaysInvisible;
import com.censoredsoftware.demigods.greek.ability.passive.Carry;
import com.censoredsoftware.demigods.greek.ability.passive.NoDamage;
import com.censoredsoftware.demigods.greek.ability.passive.RainbowWalking;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public class Lachesis extends GreekDeity
{
	public static final String NAME = "Lachesis", SHORT_DESCRIPTION = ChatColor.GRAY + "The one who measures.";
	public static final Alliance ALLIANCE = GreekAlliance.FATE;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final int ACCURACY = 15, FAVOR_REGEN = 999, MAX_FAVOR = 20000, MAX_HEALTH = 2, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.DARK_GRAY;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 3));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 10));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
	public static final List<Ability> ABILITIES = Lists.newArrayList((Ability) new NoDamage(NAME), new AlwaysInvisible(NAME), new Carry(NAME, false), new RainbowWalking(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Lachesis()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Lachesis();

	public static Deity inst()
	{
		return INST;
	}
}
