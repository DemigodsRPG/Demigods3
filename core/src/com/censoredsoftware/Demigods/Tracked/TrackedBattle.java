package com.censoredsoftware.Demigods.Tracked;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;

// TODO Convert this.

public class TrackedBattle
{
	private int battleID;
	private int whoStarted;
	private TrackedLocation startLocation;
	private ArrayList<Integer> involvedCharIDs;
	private ArrayList<TrackedLocation> involvedLocations;
	private Long startTime;
	private Long endTime;
	private boolean isActive = true;

	public TrackedBattle(PlayerCharacterClass attacking, PlayerCharacterClass defending, final Long startTime, final int battleID)
	{
		// Define variables
		Player started = (Player) attacking.getOwner();
		Location startedLocation = started.getLocation();

		this.battleID = battleID;
		this.whoStarted = attacking.getID();
		this.startLocation = new TrackedLocation(startedLocation, null);
		this.startTime = startTime;

		addCharacter(attacking);
		addCharacter(defending);

		// API.data.saveTimedData(battleID, "battle_active", true, 10); // TODO Timed data.
		save();
	}

	public void save() // TODO This shouldn't be handled here.
	{
		DemigodsData.battleData.saveData(battleID, this);
	}

	public int getID()
	{
		return this.battleID;
	}

	public void addCharacter(PlayerCharacterClass character)
	{
		addCharID(character.getID());
		if(character.getOwner().isOnline()) addLocation(character.getOwner().getPlayer().getLocation());
	}

	public void removeCharacter(PlayerCharacterClass character)
	{
		removeCharID(character.getID());
	}

	public ArrayList<Integer> getCharIDs()
	{
		return this.involvedCharIDs;
	}

	public void overwriteCharIDs(ArrayList<Integer> involvedCharIDs)
	{
		this.involvedCharIDs = involvedCharIDs;
		save();
	}

	public void addCharID(int charID)
	{
		if(this.involvedCharIDs == null) this.involvedCharIDs = new ArrayList<Integer>();
		if(!this.involvedCharIDs.contains(charID)) this.involvedCharIDs.add(charID);
		save();
	}

	public void removeCharID(int charID)
	{
		if(this.involvedCharIDs.contains(charID)) this.involvedCharIDs.remove(charID);
		save();
	}

	public ArrayList<TrackedLocation> getLocations()
	{
		return this.involvedLocations;
	}

	public void overwriteLocations(ArrayList<TrackedLocation> involvedLocations)
	{
		this.involvedLocations = involvedLocations;
		save();
	}

	public void addLocation(Location location)
	{
		if(this.involvedLocations == null) this.involvedLocations = new ArrayList<TrackedLocation>();
		if(!this.involvedLocations.contains(new TrackedLocation(location, null))) this.involvedLocations.add(new TrackedLocation(location, null));
		save();
	}

	public void removeLocation(Location location)
	{
		if(this.involvedLocations.contains(new TrackedLocation(location, null))) this.involvedLocations.remove(new TrackedLocation(location, null));
		save();
	}

	public int getWhoStarted()
	{
		return this.whoStarted;
	}

	public Long getStartTime()
	{
		return this.startTime;
	}

	public Long getEndTime()
	{
		return this.endTime;
	}

	public boolean isActive()
	{
		return this.isActive;
	}

	public synchronized void setActive(boolean active)
	{
		this.isActive = active;
	}
}
