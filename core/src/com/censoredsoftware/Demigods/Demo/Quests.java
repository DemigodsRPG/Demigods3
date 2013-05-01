package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Data.Quest.Tutorial;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;

public enum Quests implements Demigods.ListedQuest
{
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
