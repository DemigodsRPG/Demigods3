package com.censoredsoftware.demigods.greek.deity.god;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.passive.NoZombie;
import com.censoredsoftware.demigods.greek.ability.ultimate.Swarm;
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

public class Hades extends GreekDeity
{
	public static final String NAME = "Hades", SHORT_DESCRIPTION = ChatColor.GRAY + "The god of the underworld.";
	public static final Alliance ALLIANCE = GreekAlliance.GOD;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final PermissionDefault PERMISSION_DEFAULT = PermissionDefault.NOT_OP;
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 40, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.GRAY;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.ROTTEN_FLESH, 3, Material.BONE, 2));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BONE, 21));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.NON_PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList(new NoZombie(NAME), new Swarm(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	public Hades()
	{
		super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Hades();

	public static Deity inst()
	{
		return INST;
	}
}
