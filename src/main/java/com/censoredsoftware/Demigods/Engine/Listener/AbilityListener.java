package com.censoredsoftware.Demigods.Engine.Listener;

import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityEvent;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityTargetEvent;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsPlayer;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AbilityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public static void onAbility(AbilityEvent event)
	{
		// TODO Leveling.
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public static void onAbilityTarget(AbilityTargetEvent event)
	{
		if(!(event.getTarget() instanceof Player)) return;

		PlayerCharacter hitChar = DemigodsPlayer.getPlayer((Player) event.getTarget()).getCurrent();
		PlayerCharacter hittingChar = event.getCharacter();

		// TrackedBattle.battleProcess(hitChar, hittingChar);
	}
}
