package com.censoredsoftware.Demigods.Episodes.Demo.Ability.Offense;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.Demigods.Engine.Object.Ability;
import com.censoredsoftware.Demigods.Engine.Object.DPlayer;
import com.censoredsoftware.Demigods.Engine.Object.Deity;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;

public class Blaze extends Ability
{
	private final static String name = "Blaze", command = "blaze";
	private final static int cost = 400, delay = 15, repeat = 0;
	private static Info info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Ignite the ground at the target location.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.OFFENSE;

	public Blaze(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!Ability.Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
				{
					if(!DPlayer.Character.Util.isCooledDown(character, name, false)) return;

					Util.blaze(player);
				}
			}
		}, null);
	}

	public static class Util
	{
		// The actual ability command
		public static void blaze(Player player)
		{
			// Define variables
			DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();
			Location target;
			LivingEntity entity = Ability.Util.autoTarget(player);
			boolean notify;
			if(entity != null)
			{
				target = Ability.Util.autoTarget(player).getLocation();
				notify = true;
				if(!Ability.Util.doAbilityPreProcess(player, entity, name, cost, info) || entity.getEntityId() == player.getEntityId()) return;
			}
			else
			{
				target = Ability.Util.directTarget(player);
				notify = false;
				if(!Ability.Util.doAbilityPreProcess(player, name, cost, info)) return;
			}
			int power = character.getMeta().getDevotion(type).getLevel();
			int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
			if(diameter > 12) diameter = 12;

			DPlayer.Character.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
			character.getMeta().subtractFavor(cost);

			if(!Ability.Util.doTargeting(player, target, notify)) return;

			for(int X = -diameter / 2; X <= diameter / 2; X++)
			{
				for(int Y = -diameter / 2; Y <= diameter / 2; Y++)
				{
					for(int Z = -diameter / 2; Z <= diameter / 2; Z++)
					{
						Block block = target.getWorld().getBlockAt(target.getBlockX() + X, target.getBlockY() + Y, target.getBlockZ() + Z);
						if((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !ZoneUtility.zoneNoBuild(player, block.getLocation()))) block.setType(Material.FIRE);
					}
				}
			}
		}
	}
}
