package com.demigodsrpg.demigods.greek.trigger;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.data.TimedData;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Death;
import com.demigodsrpg.demigods.engine.util.Zones;
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
		Collection<DemigodsCharacter> characters = Collections2.filter(Demigods.getServer().getOnlineCharactersBelowAscension(noobAscensions), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return Death.getRecentDeaths(character, focusTime).size() >= deathsNeeded && !TimedData.exists(character.getName(), "needsHelpTrigger");
			}
		});
		if(characters.isEmpty()) return;
		for(DemigodsCharacter character : characters)
		{
			if(Zones.inNoDemigodsZone(character.getLocation())) continue;
			character.sendAllianceMessage(ChatColor.YELLOW + " " + Symbol.CAUTION + " " + character.getDeity().getColor() + character.getName() + ChatColor.YELLOW + " needs help!");
			TimedData.saveTimed(character.getName(), "needsHelpTrigger", true, focusTime, TimeUnit.MINUTES);
		}
	}

	@Override
	public void processAsync()
	{}
}
