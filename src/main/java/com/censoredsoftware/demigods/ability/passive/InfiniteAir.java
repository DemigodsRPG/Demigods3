package com.censoredsoftware.demigods.ability.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;

public class InfiniteAir extends Ability
{
	private final static String name = "InfiniteAir", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;

	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Have infinite air when moving underwater.");
		}
	};

	public InfiniteAir(final String deity, String permission)
	{
		super(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerMove(PlayerMoveEvent event)
			{
				if(Demigods.isDisabledWorld(event.getPlayer().getWorld())) return;

				Player player = event.getPlayer();
				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER)) player.setRemainingAir(20);
			}
		}, null, deity, name, command, permission, cost, delay, repeat, details, type);
	}
}
