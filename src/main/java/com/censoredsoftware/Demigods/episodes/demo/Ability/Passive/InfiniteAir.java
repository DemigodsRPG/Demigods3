package com.censoredsoftware.demigods.episodes.demo.ability.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;

public class InfiniteAir extends Ability
{
	private final static String name = "InfiniteAir", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;
	private static Info info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Have infinite air when moving underwater.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.PASSIVE;

	public InfiniteAir(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerMove(PlayerMoveEvent event)
			{
				Player player = event.getPlayer();
				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER)) player.setRemainingAir(20);
			}
		}, null);
	}
}
