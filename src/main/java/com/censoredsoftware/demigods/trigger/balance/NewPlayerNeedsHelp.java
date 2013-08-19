package com.censoredsoftware.demigods.trigger.balance;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.Death;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.ChatColor;

public class NewPlayerNeedsHelp implements Trigger
{
	public static NewPlayerNeedsHelp trigger;

	static
	{
		trigger = new NewPlayerNeedsHelp();
	}

	@Override
	public boolean evaluate() // TODO Balance this.
	{
		return Iterables.any(DCharacter.Util.getOnlineCharactersBelowAscension(DCharacter.Util.getMedianOverallAscension()), new Predicate<DCharacter>()
		{
			@Override
			public boolean apply(DCharacter character)
			{
				return Death.Util.getRecentDeaths(character, 600).size() >= 20 && !DataManager.hasTimed(character.getName(), "needsHelpTrigger");
			}
		});
	}

	public class Process implements Trigger.Process
	{
		@Override
		public void sync() // TODO Balance this.
		{
			if(!evaluate()) return;
			for(DCharacter character : Collections2.filter(DCharacter.Util.getOnlineCharactersBelowAscension(DCharacter.Util.getMedianOverallAscension()), new Predicate<DCharacter>()
			{
				@Override
				public boolean apply(DCharacter character)
				{
					return Death.Util.getRecentDeaths(character, 600).size() >= 20 && !DataManager.hasTimed(character.getName(), "needsHelpTrigger");
				}
			}))
			{
				if(Demigods.isDisabledWorld(character.getLocation())) continue;
				Demigods.message.broadcast(ChatColor.YELLOW + "Hey, " + character.getDeity().getColor() + character.getName() + ChatColor.YELLOW + " needs help!");// TODO Baetylus shards.
				DataManager.saveTimed(character.getName(), "needsHelpTrigger", true, 600);
			}
		}

		@Override
		public void async()
		{}
	}

	@Override
	public Process process()
	{
		return new Process();
	}
}
