package com.censoredsoftware.Demigods.Episodes.Demo;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Element.Deity;
import com.censoredsoftware.Demigods.Engine.Element.Structure.Structure;
import com.censoredsoftware.Demigods.Engine.Element.Task;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.God.Poseidon;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.God.Zeus;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Insignian.DrD1sco;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Insignian.OmegaX17;
import com.censoredsoftware.Demigods.Episodes.Demo.Deity.Titan.Oceanus;
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
		ZEUS(new Zeus()), POSEIDON(new Poseidon()),

		// TITANS
		OCEANUS(new Oceanus()), PROMETHEUS(new Prometheus()),

		// DONATORS
		DISCO(new DrD1sco()), OMEGA(new OmegaX17());

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
		 * The Tutorial List.
		 */
		Tutorial(new Tutorial());

		private Task.List taskSet;

		private Tasks(Task.List tasks)
		{
			this.taskSet = tasks;
		}

		@Override
		public Task.List getTaskSet()
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
