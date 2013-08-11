package com.censoredsoftware.demigods.structure;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

public interface MassiveStructure extends Structure
{
	public boolean generate(Location reference);

	@Model
	public static class Save
	{
		/**
		 * JOhm
		 */
		@Id
		private Long id;

		/**
		 * Required
		 */
		@Indexed
		@Attribute
		private String type;
		@Indexed
		@CollectionSet(of = Structure.Save.class)
		private Set<Structure.Save> parts;
		@Indexed
		@CollectionSet(of = String.class)
		private Set<String> flags;
		@Indexed
		@CollectionSet(of = String.class)
		private Set<String> regions;

		// TODO
	}
}
