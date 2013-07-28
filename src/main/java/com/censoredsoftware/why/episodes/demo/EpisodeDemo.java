package com.censoredsoftware.why.episodes.demo;

import com.censoredsoftware.why.engine.Demigods;
import com.censoredsoftware.why.engine.element.Deity;
import com.censoredsoftware.why.engine.element.Task;
import com.censoredsoftware.why.engine.element.structure.Structure;
import com.censoredsoftware.why.episodes.demo.deity.god.Poseidon;
import com.censoredsoftware.why.episodes.demo.deity.god.Zeus;
import com.censoredsoftware.why.episodes.demo.deity.insignian.DrD1sco;
import com.censoredsoftware.why.episodes.demo.deity.insignian.OmegaX17;
import com.censoredsoftware.why.episodes.demo.deity.titan.Oceanus;
import com.censoredsoftware.why.episodes.demo.deity.titan.Prometheus;
import com.censoredsoftware.why.episodes.demo.structure.Altar;
import com.censoredsoftware.why.episodes.demo.structure.Obelisk;
import com.censoredsoftware.why.episodes.demo.structure.Shrine;
import com.censoredsoftware.why.episodes.demo.task.Tutorial;

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
