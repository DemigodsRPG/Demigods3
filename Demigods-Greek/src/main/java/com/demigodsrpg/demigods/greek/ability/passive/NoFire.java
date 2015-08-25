package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NoFire extends GreekAbility.Passive {
    private static final String NAME = "No Fire Damage";
    private static final int REPEAT = 0;
    private static final List<String> DETAILS = Lists.newArrayList("Take no corruption from fire.");

    public NoFire(final String deity) {
        super(NAME, deity, REPEAT, DETAILS, null, new Runnable() {
            @Override
            public void run() {
                for (DemigodsCharacter character : Demigods.getServer().getOnlineCharactersWithAbility(NAME)) {
                    if (Zones.inNoDemigodsZone(character.getBukkitOfflinePlayer().getPlayer().getLocation())) continue;
                    Player player = character.getBukkitOfflinePlayer().getPlayer();
                    potionEffect(player);
                    if (player.isInsideVehicle() && player.getVehicle().getType().equals(EntityType.HORSE))
                        potionEffect((LivingEntity) player.getVehicle());
                }
            }

            private void potionEffect(LivingEntity entity) {
                entity.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 1));
            }
        });
    }
}
