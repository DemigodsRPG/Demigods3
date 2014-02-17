package com.demigodsrpg.demigods.greek.deity.titan;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.greek.ability.passive.Carry;
import com.demigodsrpg.demigods.greek.ability.passive.NoDrown;
import com.demigodsrpg.demigods.greek.ability.passive.Swim;
import com.demigodsrpg.demigods.greek.deity.GreekAlliance;
import com.demigodsrpg.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;

public class Oceanus extends GreekDeity
{
	public static final String NAME = "Oceanus", SHORT_DESCRIPTION = ChatColor.GRAY + "The titan of the oceans.";
	public static final Alliance ALLIANCE = GreekAlliance.TITAN;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final PermissionDefault PERMISSION_DEFAULT = null;
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 50, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.DARK_AQUA;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.RAW_FISH, 2, Material.FISHING_ROD, 1));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.COOKED_FISH, 4, Material.FISHING_ROD, 1));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList((Ability) new Swim(NAME), new NoDrown(NAME), new Carry(NAME, true));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Oceanus()
	{
		super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Oceanus();

	public static Deity inst()
	{
		return INST;
	}
}
