package com.demigodsrpg.demigods.greek;

import com.censoredsoftware.library.helper.CommandManager;
import com.censoredsoftware.library.trigger.Trigger;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.mythos.Mythos;
import com.demigodsrpg.demigods.engine.mythos.MythosPlugin;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.greek.deity.GreekAlliance;
import com.demigodsrpg.demigods.greek.deity.fate.Atropos;
import com.demigodsrpg.demigods.greek.deity.fate.Clotho;
import com.demigodsrpg.demigods.greek.deity.fate.Lachesis;
import com.demigodsrpg.demigods.greek.deity.god.Poseidon;
import com.demigodsrpg.demigods.greek.deity.god.Zeus;
import com.demigodsrpg.demigods.greek.deity.titan.Oceanus;
import com.demigodsrpg.demigods.greek.deity.titan.Perses;
import com.demigodsrpg.demigods.greek.item.armor.FaultyBootsOfHermes;
import com.demigodsrpg.demigods.greek.item.book.BookOfPrayer;
import com.demigodsrpg.demigods.greek.item.weapon.BowOfTria;
import com.demigodsrpg.demigods.greek.structure.Altar;
import com.demigodsrpg.demigods.greek.structure.Obelisk;
import com.demigodsrpg.demigods.greek.structure.Shrine;
import com.demigodsrpg.demigods.greek.trigger.NewPlayerNeedsHelp;
import com.demigodsrpg.demigods.greek.trigger.ProcessAltars;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;

public class GreekMythos extends MythosPlugin
{
	private static boolean PRIMARY = true;
	private static boolean lock = false;

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		// not really sure how Bukkit handles these, presuming the same way as EventPriority
		getServer().getServicesManager().register(Mythos.class, this, this, ServicePriority.Highest);
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
	}

	@Override
	public String getTitle()
	{
		return "Greek";
	}

	@Override
	public String getTagline()
	{
		return "Greek mythology, as described by Hesiod, Homer, and other Greek bards.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex & HmmmQuestionMark";
	}

	@Override
	public Boolean isPrimary()
	{
		return PRIMARY;
	}

	@Override
	public Boolean allowSecondary()
	{
		return true;
	}

	@Override
	public String[] getIncompatible()
	{
		return new String[] { };
	}

	@Override
	public Boolean useBaseGame()
	{
		return true;
	}

	@Override
	public ImmutableCollection<DivineItem> getDivineItems()
	{
		return ImmutableSet.of(BookOfPrayer.inst(), BowOfTria.inst(), BookOfPrayer.inst(), FaultyBootsOfHermes.inst());
	}

	@Override
	public DivineItem getDivineItem(String itemName)
	{
		return Mythos.Util.getDivineItem(this, itemName);
	}

	@Override
	public DivineItem getDivineItem(final ItemStack itemStack)
	{
		return Mythos.Util.getDivineItem(this, itemStack);
	}

	@Override
	public boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag)
	{
		return Mythos.Util.itemHasFlag(this, itemStack, flag);
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.copyOf((Alliance[]) GreekAlliance.values());
	}

	@Override
	public Alliance getAlliance(final String allianceName)
	{
		return Mythos.Util.getAlliance(this, allianceName);
	}

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.of(Zeus.inst(), Poseidon.inst(), Perses.inst(), Oceanus.inst(), Clotho.inst(), Lachesis.inst(), Atropos.inst());
	}

	@Override
	public Deity getDeity(final String deityName)
	{
		return Mythos.Util.getDeity(this, deityName);
	}

	@Override
	public ImmutableCollection<DemigodsStructureType> getStructures()
	{
		return ImmutableSet.of(Altar.inst(), Obelisk.inst(), Shrine.inst());
	}

	@Override
	public DemigodsStructureType getStructure(final String structureName)
	{
		return Mythos.Util.getStructure(this, structureName);
	}

	public Boolean levelSeperateSkills()
	{
		return true;
	}

	public ImmutableCollection<Listener> getListeners()
	{
		return ImmutableSet.of();
	}

	public ImmutableCollection<Permission> getPermissions()
	{
		return ImmutableSet.of();
	}

	@Override
	public ImmutableCollection<CommandManager> getCommands()
	{
		return ImmutableSet.of();
	}

	// private static final DivinityUnbalanced DIVINITY_UNBALANCED = new DivinityUnbalanced();
	private static final NewPlayerNeedsHelp NEW_PLAYER_NEEDS_HELP = new NewPlayerNeedsHelp();
	private static final ProcessAltars PROCESS_ALTARS = new ProcessAltars();

	public ImmutableCollection<Trigger> getTriggers()
	{
		if(!isPrimary()) return ImmutableSet.of();
		return ImmutableSet.of(/* DIVINITY_UNBALANCED, */NEW_PLAYER_NEEDS_HELP, PROCESS_ALTARS);
	}

	@Override
	public void setSecondary()
	{
		if(lock) return;
		PRIMARY = false;
	}

	@Override
	public void lock()
	{
		lock = true;
	}
}
