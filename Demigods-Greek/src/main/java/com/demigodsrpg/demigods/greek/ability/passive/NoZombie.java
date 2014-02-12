package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
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
				if(targetEvent.getEntity() instanceof Zombie && Deity.Util.canUseDeitySilent(DemigodsCharacter.of((Player) targetEvent.getTarget()), deity)) targetEvent.setCancelled(true);
			}
		}, new Runnable()
		{
			@Override
			public void run()
			{
				for(DemigodsCharacter character : Demigods.getServer().getOnlineCharactersWithDeity(deity))
				{
					Player player = character.getBukkitOfflinePlayer().getPlayer();
					for(Entity entity : player.getNearbyEntities(15, 15, 15))
						if(entity instanceof Zombie && ((Zombie) entity).getTarget() != null && ((Zombie) entity).getTarget().equals(player)) ((Zombie) entity).setTarget(null);
				}
			}
		});
	}
}
