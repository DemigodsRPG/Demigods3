package com.demigodsrpg.demigods.greek.ability.support;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Shove extends GreekAbility {
    private static final String NAME = "Shove", COMMAND = "shove";
    private static final int COST = 170, DELAY = 15, REPEAT = 0;
    private static final List<String> DETAILS = Lists.newArrayList("Shove your target away from you.");
    private static final Skill.Type TYPE = Skill.Type.SUPPORT;

    public Shove(String deity) {
        super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>() {
            @Override
            public boolean apply(Player player) {
                LivingEntity target = Ability.Util.autoTarget(player);

                if (!Ability.Util.target(player, target.getLocation(), true)) return false;

                // Define variables
                DemigodsCharacter character = DemigodsCharacter.of(player);
                int ascensions = character.getMeta().getAscensions();
                double multiply = 0.1753 * Math.pow(ascensions, 0.322917);

                Vector vector = player.getLocation().toVector();
                Vector victor = target.getLocation().toVector().subtract(vector);
                victor.multiply(multiply);
                target.setVelocity(victor);
                Ability.Util.dealDamage(player, target, 0, EntityDamageEvent.DamageCause.FALL);

                return true;
            }
        }, null, null);
    }
}
