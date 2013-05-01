package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Data.Quest.Passive.Altar;
import com.censoredsoftware.Demigods.Demo.Data.Quest.Tutorial;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;

public enum Quests implements Demigods.ListedQuest
{
	/**
	 * The Passive Altar Quest that is always active for everyone.
	 */
	ALTAR(new Altar()),

	/**
	 * The Tutorial Quest.
	 */
	TUTORIAL(new Tutorial());

	private Quest deity;

	private Quests(Quest deity)
	{
		this.deity = deity;
	}

	@Override
	public Quest getQuest()
	{
		return deity;
	}
}
