package com.demigodsrpg.demigods.greek.ability.ultimate;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Storm extends GreekAbility {
    private static final String NAME = "Storm", COMMAND = "storm";
    private static final int COST = 3700, DELAY = 300, REPEAT = 0;
    private static final List<String> DETAILS = Lists.newArrayList("Strike fear into the hearts of your enemies.");
    private static final Skill.Type TYPE = Skill.Type.ULTIMATE;

    public Storm(String deity) {
        super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>() {
            @Override
            public boolean apply(final Player player) {
                // Define variables
                DemigodsCharacter character = DemigodsCharacter.of(player);

                // Define variables
                final int ultimateSkillLevel = character.getMeta().getSkill(Skill.Type.ULTIMATE).getLevel();
                final int damage = (int) Math.ceil(8 * (int) Math.pow(ultimateSkillLevel, 0.5));
                final int radius = (int) Math.log10(10 * ultimateSkillLevel) * 25;

                // Make it stormy for the caster
                setWeather(player, 100);

                // Strike targets
                for (final Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    // Validate them first
                    if (!(entity instanceof LivingEntity)) continue;
                    if (entity instanceof Player) {
                        DemigodsCharacter opponent = DemigodsCharacter.of((Player) entity);
                        if (opponent != null && character.alliedTo(opponent)) continue;
                    }
                    if (Battle.canParticipate(entity) && !Battle.canTarget(Battle.defineParticipant(entity))) continue;

                    // Make it stormy for players
                    if (entity instanceof Player) setWeather((Player) entity, 100);

                    // Strike them with a small delay
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 3; i++) {
                                player.getWorld().strikeLightningEffect(entity.getLocation());
                                Ability.Util.dealDamage(player, (LivingEntity) entity, damage, EntityDamageEvent.DamageCause.LIGHTNING);
                            }
                        }
                    }, 15);
                }

                return true;
            }
        }, null, null);
    }

    public static void setWeather(final Player player, long ticks) {
        // Set the weather
        player.setPlayerWeather(WeatherType.DOWNFALL);

        // Create the runnable to switch back
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable() {
            @Override
            public void run() {
                player.resetPlayerWeather();
            }
        }, ticks);
    }
}
