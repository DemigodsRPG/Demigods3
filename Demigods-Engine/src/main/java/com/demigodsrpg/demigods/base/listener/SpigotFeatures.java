package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.trigger.Trigger;
import com.demigodsrpg.demigods.engine.util.Threads;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SpigotFeatures implements Listener {
    public SpigotFeatures() {
        Threads.registerTrigger(DemigodsPlugin.getInst(), new Trigger() {
            @Override
            public void processSync() {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    Location location = online.getLocation();
                    DemigodsCharacter character = DemigodsCharacter.of(online);
                    if (Zones.inNoDemigodsZone(location) || character == null || !DemigodsStructureType.Util.isInRadiusWithFlag(location, DemigodsStructureType.Flag.NO_GRIEFING)) {
                        online.spigot().setCollidesWithEntities(true);
                        continue;
                    }
                    DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(location, DemigodsStructureType.Flag.NO_GRIEFING), null);
                    if (save == null || save.getOwner().equals(character.getId())) {
                        online.spigot().setCollidesWithEntities(true);
                        continue;
                    }
                    online.spigot().setCollidesWithEntities(false);
                }
            }

            @Override
            public void processAsync() {
            }
        });
    }
}
