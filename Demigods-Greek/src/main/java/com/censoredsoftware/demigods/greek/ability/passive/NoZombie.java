package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.List;

public class NoZombie extends GreekAbility.Passive
{
	private static final String NAME = "No Zombie Damage";
	private static final int REPEAT = 0;
	private static final List<String> DETAILS = Lists.newArrayList("You cannot be damaged by zombies.");

	public NoZombie(final String deity)
	{
		super(NAME, deity, REPEAT, DETAILS, new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityDamage(EntityDamageByEntityEvent damageEvent)
			{
				if(Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives zombie corrupt, cancel it
					if(damageEvent.getDamager() instanceof Zombie) damageEvent.setCancelled(true);
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityTargetEntity(EntityTargetLivingEntityEvent targetEvent)
			{
				if(Zones.inNoDemigodsZone(targetEvent.getEntity().getLocation()) || !(targetEvent.getTarget() instanceof Player)) return;
				if(targetEvent.getEntity() instanceof Zombie && Deity.Util.canUseDeitySilent(DemigodsPlayer.Util.getPlayer((Player) targetEvent.getTarget()).getCurrent(), deity)) targetEvent.setCancelled(true);
			}
		}, new Runnable()
		{
			@Override
			public void run()
			{
				for(DemigodsCharacter character : DemigodsCharacter.Util.getOnlineCharactersWithDeity(deity))
				{
					Player player = character.getOfflinePlayer().getPlayer();
					for(Entity entity : player.getNearbyEntities(15, 15, 15))
						if(entity instanceof Zombie && ((Zombie) entity).getTarget() != null && ((Zombie) entity).getTarget().equals(player)) ((Zombie) entity).setTarget(null);
				}
			}
		});
	}
}
