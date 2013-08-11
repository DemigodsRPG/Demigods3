package com.censoredsoftware.demigods.ability.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.deity.Deity;

public class Swim extends Ability
{
	private final static String name = "Swim", command = null;
	private final static int cost = 0, delay = 0, repeat = 20;

	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Crouch while in water to swim very fast.");
		}
	};

	public Swim(final String deity, String permission)
	{
		super(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			private void onPlayerMoveEvent(PlayerMoveEvent event)
			{
				if(Demigods.isDisabledWorld(event.getPlayer().getWorld())) return;

				Player player = event.getPlayer();
				boolean inWater = player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || player.getLocation().getBlock().getType().equals(Material.WATER);

				if(player.isSneaking() && inWater && DataManager.hasKeyTemp(player.getName(), "is_swimming"))
				{
					Vector direction = player.getLocation().getDirection().normalize().multiply(1.3D);
					Vector victor = new Vector(direction.getX(), direction.getY(), direction.getZ());
					player.setVelocity(victor);
					return;
				}
				else if(!player.isSneaking() && !inWater)
				{
					DataManager.removeTemp(player.getName(), "is_swimming");
					return;
				}
				else if(player.isSneaking() && inWater && Deity.Util.canUseDeitySilent(player, deity))
				{
					DataManager.saveTemp(player.getName(), "is_swimming", true);
					return;
				}
			}
		}, null, deity, name, command, permission, cost, delay, repeat, details, type);
	}
}
