package com.censoredsoftware.Demigods.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.BattleAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Demigod.Demigod;
import com.censoredsoftware.Demigods.Event.Ability.AbilityEvent;
import com.censoredsoftware.Demigods.Event.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Event.Ability.AbilityTargetEvent;

public class AbilityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public static void onAbility(AbilityEvent event)
	{
		// Get variables
		Demigod character = event.getCharacter();
		AbilityType type = event.getType();
		int cost = event.getCost();
		int power = character.getPower(type);

		// Does this ability type gain power from being used?
		switch(type)
		{
			case OFFENSE:
				break; // Yes
			case DEFENSE:
				break; // Yes
			case STEALTH:
				return; // No
			case SUPPORT:
				return; // No
			case PASSIVE:
				return; // No
		}

		int increase = cost / power;
		if(increase < 1) increase = 1;

		// Set the new power.
		// character.setPower(type, power + increase); // TODO
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public static void onAbilityTarget(AbilityTargetEvent event)
	{
		if(!(event.getTarget() instanceof Player)) return;

		Demigod hitChar = PlayerAPI.getCurrentChar((Player) event.getTarget());
		Demigod hittingChar = event.getCharacter();

		BattleAPI.battleProcess(hitChar, hittingChar);
	}
}
