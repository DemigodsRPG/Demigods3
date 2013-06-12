package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Object.DemigodsPlayer;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Utility.BattleUtility;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		// Handle returns
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		if(DemigodsPlayer.getPlayer((Player) event.getEntity()).getCurrent() == null) return;
		if(DemigodsPlayer.getPlayer((Player) event.getDamager()).getCurrent() == null) return;

		// Define players
		Player damageePlayer = (Player) event.getEntity();
		Player damagerPlayer = (Player) event.getDamager();

		// Define characters
		PlayerCharacter damageeCharacter = DemigodsPlayer.getPlayer(damageePlayer).getCurrent();
		PlayerCharacter damagerCharacter = DemigodsPlayer.getPlayer(damagerPlayer).getCurrent();

		// Calculate midpoint location
		Location midpoint = damagerPlayer.getLocation().toVector().getMidpoint(damageePlayer.getLocation().toVector()).toLocation(damagerPlayer.getWorld());

		if(!BattleUtility.existsNear(midpoint))
		{
			Battle battle = Battle.create(damagerCharacter, damageeCharacter);

			// Debug
			Demigods.message.broadcast(ChatColor.YELLOW + "Battle started involving " + damagerCharacter.getName() + " and " + damageeCharacter.getName() + "!");
		}
		else
		{
			Battle battle = BattleUtility.getNear(midpoint);

			// Debug
			Demigods.message.broadcast(ChatColor.GREEN + "Battle exists!");
		}
	}
}
