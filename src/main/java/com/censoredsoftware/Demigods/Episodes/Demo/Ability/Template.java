package com.censoredsoftware.Demigods.Episodes.Demo.Ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.Demigods.Engine.Element.Ability;
import com.censoredsoftware.Demigods.Engine.Element.Deity;
import com.censoredsoftware.Demigods.Engine.Player.DCharacter;
import com.censoredsoftware.Demigods.Engine.Player.DPlayer;

public class Template extends Ability
{
	private final static String name = "Test", command = "test";
	private final static int cost = 170, delay = 1500, repeat = 0;
	private static Info info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Test your target.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.SUPPORT;

	public Template(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!Ability.Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.test(player);
				}
			}
		}, null);
	}

	public static class Util
	{
		// The actual ability command
		public static void test(Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			LivingEntity target = Ability.Util.autoTarget(player);

			if(!Ability.Util.doAbilityPreProcess(player, target, "test", cost, info)) return;
			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
			character.getMeta().subtractFavor(cost);

			if(!Ability.Util.doTargeting(player, target.getLocation(), true)) return;

			if(target instanceof Player)
			{
				Player victim = (Player) target;
				victim.sendMessage("Test!");
				player.sendMessage("Tested " + victim.getName() + "!");
			}
		}
	}
}
