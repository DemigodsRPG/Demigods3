package com.censoredsoftware.Demigods.Engine.Utility;

import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacterMeta;

/**
 * Handles all leveling of
 */
public class LevelingUtility
{
	public static void processAbility(PlayerCharacter character, Ability.Type type)
	{
		// Define variables
		PlayerCharacterMeta meta = character.getMeta();
		int currentLevel = meta.getLevel(type.name());

	}
}
