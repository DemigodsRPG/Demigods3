package com.demigodsrpg.demigods.engine.battle;

import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface Participant
{
	UUID getId();

	boolean canPvp();

	Location getCurrentLocation();

	DemigodsCharacter getRelatedCharacter();

	LivingEntity getEntity();
}
