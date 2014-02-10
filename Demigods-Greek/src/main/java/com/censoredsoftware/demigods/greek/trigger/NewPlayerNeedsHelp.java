package com.censoredsoftware.demigods.greek.trigger;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class NewPlayerNeedsHelp implements Trigger
{
	public static int focusTime, deathsNeeded, noobAscensions;

	static
	{
		focusTime = 40;
		deathsNeeded = 7;
		noobAscensions = 3;
	}

	@Override
	public void processSync()
	{
		Collection<DemigodsCharacter> characters = Collections2.filter(DemigodsCharacter.Util.getOnlineCharactersBelowAscension(noobAscensions), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return DDeath.Util.getRecentDeaths(character, focusTime).size() >= deathsNeeded && !Data.TIMED.boolContainsKey(character.getName() + "needsHelpTrigger");
			}
		});
		if(characters.isEmpty()) return;
		for(DemigodsCharacter character : characters)
		{
			if(Zones.inNoDemigodsZone(character.getLocation())) continue;
			character.sendAllianceMessage(ChatColor.YELLOW + " " + Symbol.CAUTION + " " + character.getDeity().getColor() + character.getName() + ChatColor.YELLOW + " needs help!");
			Data.TIMED.setBool(character.getName() + "needsHelpTrigger", true, focusTime, TimeUnit.MINUTES);
		}
	}

	@Override
	public void processAsync()
	{}
}
