package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.censoredlib.util.Threads;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SpigotFeatures implements Listener
{
	public SpigotFeatures()
	{
		Threads.registerTrigger(DemigodsPlugin.getInst(), new Trigger()
		{
			@Override
			public void processSync()
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					Location location = online.getLocation();
					DemigodsCharacter character = DemigodsPlayer.Util.getPlayer(online).getCurrent();
					if(Zones.inNoDemigodsZone(location) || character == null || !StructureType.Util.isInRadiusWithFlag(location, StructureType.Flag.NO_GRIEFING))
					{
						online.spigot().setCollidesWithEntities(true);
						continue;
					}
					StructureSave save = Iterables.getFirst(StructureType.Util.getInRadiusWithFlag(location, StructureType.Flag.NO_GRIEFING), null);
					if(save == null || save.getOwner().equals(character.getId()))
					{
						online.spigot().setCollidesWithEntities(true);
						continue;
					}
					online.spigot().setCollidesWithEntities(false);
				}

			}

			@Override
			public void processAsync()
			{}
		});
	}
}
