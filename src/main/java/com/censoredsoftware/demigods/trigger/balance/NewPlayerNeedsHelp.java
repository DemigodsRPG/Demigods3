package com.censoredsoftware.demigods.trigger.balance;

import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.Death;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;

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
			public boolean apply(@Nullable DCharacter character)
			{
				return Death.Util.getRecentDeaths(character, 600).size() >= 20;
			}
		});
	}

	public class Process implements Trigger.Process
	{
		@Override
		public void sync()
		{
			if(!evaluate()) return;
			// TODO Baetylus shards.
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
