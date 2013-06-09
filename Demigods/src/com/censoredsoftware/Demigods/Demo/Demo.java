package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Deity.God.Poseidon;
import com.censoredsoftware.Demigods.Demo.Deity.God.Zeus;
import com.censoredsoftware.Demigods.Demo.Deity.Titan.Prometheus;
import com.censoredsoftware.Demigods.Demo.Quest.Passive.AltarQuest;
import com.censoredsoftware.Demigods.Demo.Quest.Passive.ShrineQuest;
import com.censoredsoftware.Demigods.Demo.Quest.Tutorial;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;
import com.censoredsoftware.Demigods.Engine.Structure.Structure;

public class Demo
{
	public static enum Deities implements Demigods.ListedDeity
	{
		// GODS
		ZEUS(new Zeus()), POSEIDON(new Poseidon()),

		// TITANS
		PROMETHEUS(new Prometheus());

		private Deity deity;

		private Deities(Deity deity)
		{
			this.deity = deity;
		}

		@Override
		public Deity getDeity()
		{
			return deity;
		}
	}

	public static enum Quests implements Demigods.ListedQuest
	{
		/**
		 * The Passive Altar Quest that is always active for everyone along with
		 * the Passive Shrine Quest.
		 */
		Altar(new AltarQuest()), Shrine(new ShrineQuest()),

		/**
		 * The Tutorial Quest.
		 */
		Tutorial(new Tutorial());

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

	public static enum Structures implements Demigods.ListedStructure
	{
		; // NOTHING YET

		@Override
		public Structure getQuest()
		{
			return null;
		}
	}
}
