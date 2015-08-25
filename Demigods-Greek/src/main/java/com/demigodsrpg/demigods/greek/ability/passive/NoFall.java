package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class NoFall extends GreekAbility.Passive {
    private static final String NAME = "No Fall Damage";
    private static final int REPEAT = 0;
    private static final List<String> DETAILS = Lists.newArrayList("Take no corruption from falling.");

    public NoFall(final String deity) {
        super(NAME, deity, REPEAT, DETAILS, new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onEntityDamage(EntityDamageEvent damageEvent) {
                if (Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
                if (damageEvent.getEntity() instanceof Player) {
                    Player player = (Player) damageEvent.getEntity();
                    if (!Deity.Util.canUseDeitySilent(player, deity)) return;

                    // If the player receives falling corrupt, cancel it
                    if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) damageEvent.setCancelled(true);
                }
            }
        }, null);
    }
}
