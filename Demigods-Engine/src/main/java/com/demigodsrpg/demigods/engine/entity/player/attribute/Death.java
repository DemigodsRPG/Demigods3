package com.demigodsrpg.demigods.engine.entity.player.attribute;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.battle.Participant;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Death extends DataAccess<UUID, Death> {
    private UUID id;
    private long deathTime;
    private UUID killed, attacking;

    private Death() {
    }

    public static Death create(Participant killed) {
        Death death = new Death();
        death.deathTime = System.currentTimeMillis();
        death.id = UUID.randomUUID();
        death.killed = killed.getRelatedCharacter().getId();
        death.save();
        return death;
    }

    public static Death create(Participant killed, Participant attacking) {
        Death death = new Death();
        death.deathTime = System.currentTimeMillis();
        death.id = UUID.randomUUID();
        death.killed = killed.getRelatedCharacter().getId();
        death.attacking = attacking.getRelatedCharacter().getId();
        death.save();
        return death;
    }

    @Register(idType = IdType.UUID)
    public Death(UUID id, ConfigurationSection conf) {
        this.id = id;
        deathTime = conf.getLong("deathTime");
        killed = UUID.fromString(conf.getString("killed"));
        if (conf.isString("attacking")) attacking = UUID.fromString(conf.getString("attacking"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("deathTime", deathTime);
        map.put("killed", killed.toString());
        if (attacking != null) map.put("attacking", attacking.toString());
        return map;
    }

    public UUID getId() {
        return id;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public UUID getKilled() {
        return killed;
    }

    public UUID getAttacking() {
        return attacking;
    }

    private static final DataAccess<UUID, Death> DATA_ACCESS = new Death();

    public static Death get(UUID id) {
        return DATA_ACCESS.getDirect(id);
    }

    public static Collection<Death> all() {
        return DATA_ACCESS.allDirect();
    }

    public static Set<Death> getRecentDeaths(int seconds) {
        final long time = System.currentTimeMillis() - (seconds * 1000);
        return Sets.newHashSet(Iterables.filter(Iterables.concat(Collections2.transform(Demigods.getOnlineCharacters(), new Function<DemigodsCharacter, Collection<Death>>() {
            @Override
            public Collection<Death> apply(DemigodsCharacter character) {
                try {
                    return character.getDeaths();
                } catch (java.lang.Exception ignored) {
                }
                return null;
            }
        })), new Predicate<Death>() {
            @Override
            public boolean apply(Death death) {
                return death.getDeathTime() >= time;
            }
        }));
    }

    public static Collection<Death> getRecentDeaths(DemigodsCharacter character, int seconds) {
        final long time = System.currentTimeMillis() - (seconds * 1000);
        return Collections2.filter(character.getDeaths(), new Predicate<Death>() {
            @Override
            public boolean apply(Death death) {
                return death.getDeathTime() >= time;
            }
        });
    }
}
