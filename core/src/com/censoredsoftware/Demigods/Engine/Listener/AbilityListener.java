package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.BattleAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityEvent;
import com.censoredsoftware.Demigods.Engine.Event.Ability.AbilityTargetEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class AbilityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public static void onAbility(AbilityEvent event)
	{
		// Get variables
		PlayerCharacter character = event.getCharacter();
		Ability.Type type = event.getType();
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

		PlayerCharacter hitChar = PlayerAPI.getCurrentChar((Player) event.getTarget());
		PlayerCharacter hittingChar = event.getCharacter();

		BattleAPI.battleProcess(hitChar, hittingChar);
	}
}
