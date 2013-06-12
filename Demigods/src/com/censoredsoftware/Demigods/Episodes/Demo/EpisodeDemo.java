package com.censoredsoftware.Demigods.Episodes.Demo;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Task.TaskSet;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.God.Poseidon;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.God.Zeus;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Titan.Oceanus;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Titan.Prometheus;
import com.censoredsoftware.Demigods.Episodes.Demo.Task.Static.AltarTasks;
import com.censoredsoftware.Demigods.Episodes.Demo.Task.Static.ShrineTasks;
import com.censoredsoftware.Demigods.Episodes.Demo.Task.Tutorial;

public class EpisodeDemo
{
	public static enum Deities implements Demigods.ListedDeity
	{
		// GODS
		ZEUS(new Zeus()), POSEIDON(new Poseidon()),

		// TITANS
		OCEANUS(new Oceanus()), PROMETHEUS(new Prometheus());

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
		 * The Passive Altar TaskSet that is always active for everyone along with
		 * the Passive Shrine TaskSet.
		 */
		Altar(new AltarTasks()), Shrine(new ShrineTasks()),

		/**
		 * The Tutorial TaskSet.
		 */
		Tutorial(new Tutorial());

		private TaskSet deity;

		private Quests(TaskSet deity)
		{
			this.deity = deity;
		}

		@Override
		public TaskSet getQuest()
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
