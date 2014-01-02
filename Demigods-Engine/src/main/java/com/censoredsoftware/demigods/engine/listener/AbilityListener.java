package com.censoredsoftware.demigods.engine.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class AbilityListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation()) || !Ability.Util.isLeftClick(interactEvent)) return;

		// Set variables
		final Player player = interactEvent.getPlayer();
		final DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		if(character == null || !character.isUsable()) return;

		for(Ability ability : Collections2.filter(character.getAbilities(), new Predicate<Ability>()
		{
			@Override
			public boolean apply(Ability ability)
			{
				return ability.getActionPredicate() != null && Deity.Util.canUseDeitySilent(character, ability.getDeity()) && player.getItemInHand() != null && character.getMeta().checkBound(ability.getName(), player.getItemInHand().getData());
			}
		}))
		{
			// Can they do it?
			if(!Ability.Util.preProcessAbility(player, ability)) continue;

			// Check for weapons
			if(ability.hasWeapon() && !player.getItemInHand().getData().equals(ability.getWeapon())) continue;

			// Process the ability
			if(ability.getActionPredicate().apply(player)) Ability.Util.postProcessAbility(character, ability);
		}
	}
}
