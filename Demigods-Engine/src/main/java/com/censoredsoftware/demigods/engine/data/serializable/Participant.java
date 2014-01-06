package com.censoredsoftware.demigods.engine.data.serializable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface Participant
{
	UUID getId();

	boolean canPvp();

	Location getCurrentLocation();

	DCharacter getRelatedCharacter();

	LivingEntity getEntity();
}
