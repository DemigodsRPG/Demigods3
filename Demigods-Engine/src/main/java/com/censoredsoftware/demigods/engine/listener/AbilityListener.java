package com.censoredsoftware.demigods.engine.listener;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbilityListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation()) || !Abilities.isLeftClick(interactEvent)) return;

		// Set variables
		final Player player = interactEvent.getPlayer();
		final DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		for(Ability ability : Collections2.filter(character.getAbilities(), new Predicate<Ability>()
		{
			@Override
			public boolean apply(Ability ability)
			{
				return ability.getActionPredicate() != null && Deity.Util.canUseDeitySilent(character, ability.getDeity()) && player.getItemInHand() != null && character.getMeta().checkBound(ability.getName(), player.getItemInHand().getData()) && DCharacter.Util.isCooledDown(character, ability.getName(), false);
			}
		}))
		{
			// Check for weapons
			if(ability.hasWeapon() && !player.getItemInHand().getData().equals(ability.getWeapon())) continue;

			// Process the cost and cooldown
			if(ability.getActionPredicate().apply(player))
			{
				Abilities.postProcessAbility(character, ability);
				if(ability.getDelay() > 0) Abilities.activateCooldown(character, ability);
			}
		}
	}
}