package com.censoredsoftware.demigods.episodes.demo.ability.offense;

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

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;

public class Shove extends Ability
{
	private final static String deity = "Zeus", name = "Shove", command = "shove", permission = "demigods.god.zeus";
	private final static int cost = 170, delay = 15, repeat = 0;
	private static Info info;
	private final static Devotion.Type type = Devotion.Type.DEFENSE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Shove your target away from you.");
		}
	};

	public Shove()
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Demigods.isDisabledWorld(interactEvent.getPlayer().getWorld())) return;

				if(!Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					shove(player);
				}
			}
		}, null);
	}

	// The actual ability command
	public static void shove(Player player)
	{
		// Define variables
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
		int ascensions = character.getMeta().getAscensions();
		double multiply = 0.1753 * Math.pow(ascensions, 0.322917);
		LivingEntity target = Util.autoTarget(player);

		if(!Util.doAbilityPreProcess(player, target, "shove", cost, info)) return;
		DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!Util.doTargeting(player, target.getLocation(), true)) return;

		Vector vector = player.getLocation().toVector();
		Vector victor = target.getLocation().toVector().subtract(vector);
		victor.multiply(multiply);
		target.setVelocity(victor);
		Util.dealDamage(player, target, 0, EntityDamageEvent.DamageCause.FALL);
	}
}
