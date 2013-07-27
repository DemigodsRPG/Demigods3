package com.censoredsoftware.Demigods.Episodes.Demo.Ability.Offense;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Engine.Object.Ability;
import com.censoredsoftware.Demigods.Engine.Object.DPlayer;
import com.censoredsoftware.Demigods.Engine.Object.Deity;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 7/26/13
 * Time: 10:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Shove extends Ability
{
	private final static String deity = "Zeus", name = "Shove", command = "shove", permission = "demigods.god.zeus";
	private final static int cost = 170, delay = 15, repeat = 0;
	private static Info info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Shove your target away from you.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.DEFENSE;

	public Shove()
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
				{
					if(!DPlayer.Character.Util.isCooledDown(character, name, false)) return;

					shove(player);
				}
			}
		}, null);
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		DPlayer.Character character = DPlayer.Util.getPlayer(player).getCurrent();
		int ascensions = character.getMeta().getAscensions();
		double multiply = 0.1753 * Math.pow(ascensions, 0.322917);
		LivingEntity target = Util.autoTarget(player);

		if(!Util.doAbilityPreProcess(player, target, "shove", cost, info)) return;
		DPlayer.Character.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!Util.doTargeting(player, target.getLocation(), true)) return;

		Vector vector = player.getLocation().toVector();
		Vector victor = target.getLocation().toVector().subtract(vector);
		victor.multiply(multiply);
		target.setVelocity(victor);
		Util.dealDamage(player, target, 0, EntityDamageEvent.DamageCause.FALL);
	}
}
