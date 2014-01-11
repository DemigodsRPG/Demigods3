package com.censoredsoftware.demigods.greek.deity.god;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.offense.Lightning;
import com.censoredsoftware.demigods.greek.ability.passive.NoFall;
import com.censoredsoftware.demigods.greek.ability.support.Shove;
import com.censoredsoftware.demigods.greek.ability.ultimate.Storm;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;

public class Zeus extends GreekDeity
{
	public static final String NAME = "Zeus", SHORT_DESCRIPTION = ChatColor.GRAY + "The god of the sky.";
	public static final Alliance ALLIANCE = GreekAlliance.GOD;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final PermissionDefault PERMISSION_DEFAULT = PermissionDefault.NOT_OP;
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 30, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.YELLOW;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FEATHER, 3));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FEATHER, 10));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList(new NoFall(NAME), new Shove(NAME), new Lightning(NAME), new Storm(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Zeus()
	{
		super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Zeus();

	public static Deity inst()
	{
		return INST;
	}
}
