package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterBetrayCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsPlayer;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;

public class KillListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof Player)
		{
			Player player = (Player) entity;
			PlayerCharacter playerChar = DemigodsPlayer.getPlayer(player).getCurrent();

			EntityDamageEvent damageEvent = player.getLastDamageCause();

			if(damageEvent instanceof EntityDamageByEntityEvent)
			{
				EntityDamageByEntityEvent damageByEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = damageByEvent.getDamager();

				if(damager instanceof Player)
				{
					Player attacker = (Player) damager;
					PlayerCharacter attackChar = DemigodsPlayer.getPlayer(attacker).getCurrent();
					if(attackChar != null && playerChar != null && PlayerCharacter.areAllied(attackChar, playerChar)) Bukkit.getServer().getPluginManager().callEvent(new CharacterBetrayCharacterEvent(attackChar, playerChar, DemigodsPlayer.getCurrentAlliance(player)));
					else Bukkit.getServer().getPluginManager().callEvent(new CharacterKillCharacterEvent(attackChar, playerChar));
				}
			}
		}
	}
}
