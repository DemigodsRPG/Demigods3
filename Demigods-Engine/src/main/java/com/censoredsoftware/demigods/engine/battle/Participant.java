package com.censoredsoftware.demigods.engine.battle;

import com.censoredsoftware.demigods.engine.player.DCharacter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface Participant
{
	public UUID getId();

	public boolean canPvp();

	public Location getCurrentLocation();

	public DCharacter getRelatedCharacter();

	public LivingEntity getEntity();
}
