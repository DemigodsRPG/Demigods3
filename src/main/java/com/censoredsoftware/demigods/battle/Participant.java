package com.censoredsoftware.demigods.battle;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.censoredsoftware.demigods.player.DCharacter;

public interface Participant
{
	public Long getId();

	public void setCanPvp(boolean pvp);

	public Boolean canPvp();

	public Location getCurrentLocation();

	public DCharacter getRelatedCharacter();

	public LivingEntity getEntity();
}
