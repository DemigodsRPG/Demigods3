package com.censoredsoftware.demigods.battle;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface Participant
{
	public UUID getId();

	public void setCanPvp(boolean pvp);

	public boolean canPvp();

	public Location getCurrentLocation();

	public com.censoredsoftware.demigods.player.Character getRelatedCharacter();

	public LivingEntity getEntity();
}
