package com.censoredsoftware.Demigods.Episodes.Demo;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Object.Task.TaskSet;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Titan.Prometheus;
import com.censoredsoftware.Demigods.Episodes.Demo.Structure.Altar;
import com.censoredsoftware.Demigods.Episodes.Demo.Structure.Obelisk;
import com.censoredsoftware.Demigods.Episodes.Demo.Structure.Shrine;
import com.censoredsoftware.Demigods.Episodes.Demo.Task.Tutorial;

public class EpisodeDemo
{
	public static enum Deities implements Demigods.ListedDeity
	{
		// GODS
		// ZEUS(new Zeus()),
		// POSEIDON(new Poseidon()),

		// TITANS
		// OCEANUS(new Oceanus()),
		PROMETHEUS(new Prometheus()),

		// DONATORS
		// DISCO(new DrD1sco()), OMEGA(new OmegaX17())
		;

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

	public static enum Tasks implements Demigods.ListedTaskSet
	{
		/**
		 * The Tutorial TaskSet.
		 */
		Tutorial(new Tutorial());

		private TaskSet taskSet;

		private Tasks(TaskSet tasks)
		{
			this.taskSet = tasks;
		}

		@Override
		public TaskSet getTaskSet()
		{
			return taskSet;
		}
	}

	public static enum Structures implements Demigods.ListedStructure
	{
		ALTAR(new Altar()), SHRINE(new Shrine()), OBELISK(new Obelisk());

		private Structure structure;

		private Structures(Structure structure)
		{
			this.structure = structure;
		}

		@Override
		public Structure getStructure()
		{
			return structure;
		}
	}
}
