package com.censoredsoftware.Demigods.Default;

import com.censoredsoftware.Demigods.Default.Data.Quest.Passive.AltarQuest;
import com.censoredsoftware.Demigods.Default.Data.Quest.Passive.ShrineQuest;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;

public enum Quests implements Demigods.ListedQuest
{
	/**
	 * The Passive Altar Quest that is always active for everyone along with
	 * the Passive Shrine Quest.
	 */
	Altar(new AltarQuest()), Shrine(new ShrineQuest());

	/**
	 * The Tutorial Quest.
	 */
	// Tutorial(new Tutorial());

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