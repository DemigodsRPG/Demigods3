package com.demigodsrpg.demigods.greek.deity.fate;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.greek.ability.offense.Fireball;
import com.demigodsrpg.demigods.greek.ability.passive.AlwaysInvisible;
import com.demigodsrpg.demigods.greek.ability.passive.Carry;
import com.demigodsrpg.demigods.greek.ability.passive.NoDamage;
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

public class Atropos extends GreekDeity {
    public static final String NAME = "Atropos", SHORT_DESCRIPTION = ChatColor.GRAY + "The one who cuts the string.";
    public static final Alliance ALLIANCE = GreekAlliance.FATE;
    public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
    public static final PermissionDefault PERMISSION_DEFAULT = PermissionDefault.NOT_OP;
    public static final int ACCURACY = 15, FAVOR_REGEN = 999, MAX_FAVOR = 20000, MAX_HEALTH = 2, FAVOR_BANK = 10000;
    public static final ChatColor COLOR = ChatColor.DARK_RED;
    public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 3));
    public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 10));
    public static final List<String> LORE = Arrays.asList();
    public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
    public static final List<Ability> ABILITIES = Lists.newArrayList(new NoDamage(NAME), new AlwaysInvisible(NAME), new Fireball(NAME), new Carry(NAME, false));

    // Mood Manager
    private static EnumMap<Deity.Mood, Deity.MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

    private Atropos() {
        super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
    }

    private static final Deity INST = new Atropos();

    public static Deity inst() {
        return INST;
    }
}
