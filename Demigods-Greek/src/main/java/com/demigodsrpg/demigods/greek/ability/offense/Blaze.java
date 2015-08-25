package com.demigodsrpg.demigods.greek.ability.offense;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class Blaze extends GreekAbility {
    private static final String NAME = "Blaze", COMMAND = "blaze";
    private static final int COST = 400, DELAY = 15, REPEAT = 0;
    private static final List<String> DETAILS = Lists.newArrayList("Ignite the ground at the target location.");
    private static final Skill.Type TYPE = Skill.Type.OFFENSE;

    public Blaze(String deity) {
        super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>() {
            @Override
            public boolean apply(Player player) {
                // Define variables
                DemigodsCharacter character = DemigodsCharacter.of(player);
                Location target;
                LivingEntity entity = Ability.Util.autoTarget(player);
                boolean notify;
                if (entity != null) {
                    target = Ability.Util.autoTarget(player).getLocation();
                    notify = true;
                    if (entity.getEntityId() == player.getEntityId()) return false;
                } else {
                    target = Ability.Util.directTarget(player);
                    notify = false;
                }
                int power = character.getMeta().getSkill(Skill.Type.OFFENSE).getLevel();
                int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
                if (diameter > 12) diameter = 12;

                if (!Ability.Util.target(player, target, notify)) return false;

                for (int X = -diameter / 2; X <= diameter / 2; X++) {
                    for (int Y = -diameter / 2; Y <= diameter / 2; Y++) {
                        for (int Z = -diameter / 2; Z <= diameter / 2; Z++) {
                            Block block = target.getWorld().getBlockAt(target.getBlockX() + X, target.getBlockY() + Y, target.getBlockZ() + Z);
                            if ((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !Zones.inNoBuildZone(player, block.getLocation())))
                                block.setType(Material.FIRE);
                        }
                    }
                }

                return true;
            }
        }, null, null);
    }
}
