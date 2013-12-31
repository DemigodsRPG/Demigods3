package com.censoredsoftware.demigods.engine.data;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface Participant
{
	UUID getId();

	boolean canPvp();

	Location getCurrentLocation();

	DCharacter getRelatedCharacter();

	LivingEntity getEntity();
}
