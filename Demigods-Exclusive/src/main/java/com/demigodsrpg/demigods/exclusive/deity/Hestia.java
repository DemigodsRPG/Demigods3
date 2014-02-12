package com.demigodsrpg.demigods.exclusive.deity;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
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

public class Hestia extends GreekDeity
{
	public static final String NAME = "Hestia", SHORT_DESCRIPTION = ChatColor.GRAY + "The goddess of architecture and government.";
	public static final Alliance ALLIANCE = GreekAlliance.GOD;
	public static final String PERMISSION = "demigods.official." + NAME;
	public static final PermissionDefault PERMISSION_DEFAULT = PermissionDefault.OP;
	public static final int ACCURACY = 1, FAVOR_REGEN = 1, MAX_FAVOR = 1, MAX_HEALTH = 20, FAVOR_BANK = 1;
	public static final ChatColor COLOR = ChatColor.DARK_PURPLE;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BOOK_AND_QUILL, 1, Material.BEDROCK, 2));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.PORK, 5));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Flag.MAJOR_DEITY, Flag.NO_BATTLE, Flag.NO_OBELISK, Flag.NO_SHRINE, Flag.NEUTRAL, Flag.PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList();

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Hestia()
	{
		super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Hestia();

	public static Deity inst()
	{
		return INST;
	}
}
