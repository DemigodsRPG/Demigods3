package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.player.SavedPotion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class DSavedPotion extends SavedPotion
{
	public DSavedPotion(PotionEffect effect)
	{
		super(effect);
	}

	public DSavedPotion(UUID id, ConfigurationSection conf)
	{
		super(id, conf);
	}

	protected void save()
	{
		DataManager.savedPotions.put(getId(), this);
	}
}
