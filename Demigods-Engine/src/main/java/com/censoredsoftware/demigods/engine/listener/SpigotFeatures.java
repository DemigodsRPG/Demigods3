package com.censoredsoftware.demigods.engine.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.censoredlib.util.Threads;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.StructureData;
import com.censoredsoftware.demigods.engine.mythos.Structure;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Iterables;

public class SpigotFeatures implements Listener
{
	public SpigotFeatures()
	{
		Threads.registerTrigger(DemigodsPlugin.plugin(), new Trigger()
		{
			@Override
			public void processSync()
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					Location location = online.getLocation();
					DCharacter character = DPlayer.Util.getPlayer(online).getCurrent();
					if(Zones.inNoDemigodsZone(location) || character == null || !Structure.Util.isInRadiusWithFlag(location, Structure.Flag.NO_GRIEFING))
					{
						online.spigot().setCollidesWithEntities(true);
						continue;
					}
					StructureData save = Iterables.getFirst(Structure.Util.getInRadiusWithFlag(location, Structure.Flag.NO_GRIEFING), null);
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
