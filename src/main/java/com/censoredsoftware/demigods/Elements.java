package com.censoredsoftware.demigods;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.censoredsoftware.core.bukkit.ListedConversation;
import com.censoredsoftware.demigods.conversation.Prayer;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.deity.god.Poseidon;
import com.censoredsoftware.demigods.deity.god.Zeus;
import com.censoredsoftware.demigods.deity.insignian.DrD1sco;
import com.censoredsoftware.demigods.deity.insignian.OmegaX17;
import com.censoredsoftware.demigods.deity.titan.Oceanus;
import com.censoredsoftware.demigods.deity.titan.Prometheus;
import com.censoredsoftware.demigods.structure.Altar;
import com.censoredsoftware.demigods.structure.Obelisk;
import com.censoredsoftware.demigods.structure.Shrine;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.task.Task;
import com.censoredsoftware.demigods.task.Tutorial;

public class Elements
{
	public static enum Deities implements ListedDeity
	{
		// GODS
		ZEUS("Zeus", new Zeus()), POSEIDON("Poseidon", new Poseidon()),

		// TITANS
		OCEANUS("Oceanus", new Oceanus()), PROMETHEUS("Prometheus", new Prometheus()),

		// DONATORS
		DISCO("DrD1sco", new DrD1sco()), OMEGA("OmegaX17", new OmegaX17());

		private final String name;
		private final Deity deity;

		private Deities(String name, Deity deity)
		{
			this.name = name;
			this.deity = deity;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public Deity getDeity()
		{
			return deity;
		}

		public static Deity get(String name)
		{
			for(ListedDeity deity : values())
				if(deity.getName().equalsIgnoreCase(name)) return deity.getDeity();
			return null;
		}
	}

	public static enum Tasks implements ListedTaskSet
	{
		/**
		 * The Tutorial List.
		 */
		Tutorial(new Tutorial());

		private final Task.List taskSet;

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

	public static enum Structures implements ListedStructure
	{
		ALTAR(new Altar()), SHRINE(new Shrine()), OBELISK(new Obelisk());

		private final Structure structure;

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

	public enum Conversations implements ListedConversation.ConversationData
	{
		// NoGrief
		PRAYER(new Prayer());

		private final ListedConversation conversationInfo;

		private Conversations(ListedConversation conversationInfo)
		{
			this.conversationInfo = conversationInfo;
		}

		public ListedConversation getConversation()
		{
			return this.conversationInfo;
		}

		// Can't touch this. Naaaaaa na-na-na.. Ba-dum, ba-dum.
		public static interface Category extends Prompt
		{
			public String getChatName();

			public boolean canUse(ConversationContext context);
		}
	}

	public interface ListedDeity
	{
		public String getName();

		public Deity getDeity();
	}

	public interface ListedTaskSet
	{
		public Task.List getTaskSet();
	}

	public interface ListedStructure
	{
		public Structure getStructure();
	}
}
