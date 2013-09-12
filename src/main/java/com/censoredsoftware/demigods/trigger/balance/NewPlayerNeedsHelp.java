package com.censoredsoftware.demigods.trigger.balance;

import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.language.Symbol;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.Death;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.censoredsoftware.demigods.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.bukkit.ChatColor;

import java.util.Collection;

public class NewPlayerNeedsHelp implements Trigger
{
	public static final NewPlayerNeedsHelp trigger = new NewPlayerNeedsHelp();
	public static int focusTime, deathsNeeded, noobAscensions;

	static
	{
		focusTime = 2400;
		deathsNeeded = 7;
		noobAscensions = 3;
	}

	@Override
	public void processSync()
	{
		Collection<DCharacter> characters = Collections2.filter(DCharacter.Util.getOnlineCharactersBelowAscension(noobAscensions), new Predicate<DCharacter>()
		{
			@Override
			public boolean apply(DCharacter character)
			{
				return Death.Util.getRecentDeaths(character, focusTime).size() >= deathsNeeded && !DataManager.hasTimed(character.getName(), "needsHelpTrigger");
			}
		});
		if(characters.isEmpty()) return;
		for(DCharacter character : characters)
		{
			if(Zones.inNoDemigodsZone(character.getLocation())) continue;
			character.sendAllianceMessage(ChatColor.YELLOW + " " + Symbol.CAUTION + " " + character.getDeity().getColor() + character.getName() + ChatColor.YELLOW + " needs help!");
			DataManager.saveTimed(character.getName(), "needsHelpTrigger", true, focusTime);
		}
	}

	@Override
	public void processAsync()
	{}
}
