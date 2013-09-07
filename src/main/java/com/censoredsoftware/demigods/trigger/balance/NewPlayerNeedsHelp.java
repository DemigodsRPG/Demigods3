package com.censoredsoftware.demigods.trigger.balance;

import java.util.Collection;

import org.bukkit.ChatColor;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.language.Symbol;
import com.censoredsoftware.demigods.player.Character;
import com.censoredsoftware.demigods.player.Death;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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
		Collection<Character> characters = Collections2.filter(Character.Util.getOnlineCharactersBelowAscension(noobAscensions), new Predicate<Character>()
		{
			@Override
			public boolean apply(Character character)
			{
				return Death.Util.getRecentDeaths(character, focusTime).size() >= deathsNeeded && !DataManager.hasTimed(character.getName(), "needsHelpTrigger");
			}
		});
		if(characters.isEmpty()) return;
		for(com.censoredsoftware.demigods.player.Character character : characters)
		{
			if(Demigods.MiscUtil.isDisabledWorld(character.getLocation())) continue;
			character.sendAllianceMessage(ChatColor.YELLOW + " " + Symbol.CAUTION + " " + character.getDeity().getColor() + character.getName() + ChatColor.YELLOW + " needs help!");
			DataManager.saveTimed(character.getName(), "needsHelpTrigger", true, focusTime);
		}
	}

	@Override
	public void processAsync()
	{}
}
