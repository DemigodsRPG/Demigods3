package com.censoredsoftware.Objects.Character.Persistence;

// TODO Move these somewhere else if I can.

import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Objects.Character.PlayerCharacter;

public class PlayerCharacterSave
{
	private Plugin plugin;
	boolean yaml;

	public PlayerCharacterSave(Plugin instance, boolean yaml)
	{
		this.plugin = instance;
		this.yaml = yaml;
	}

	public void save(PlayerCharacter character)
	{

	}
}
