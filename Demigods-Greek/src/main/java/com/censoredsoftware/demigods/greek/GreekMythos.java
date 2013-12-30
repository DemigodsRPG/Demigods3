package com.censoredsoftware.demigods.greek;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.mythos.Mythos;
import com.censoredsoftware.demigods.engine.mythos.MythosPlugin;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.fate.Atropos;
import com.censoredsoftware.demigods.greek.deity.fate.Clotho;
import com.censoredsoftware.demigods.greek.deity.fate.Lachesis;
import com.censoredsoftware.demigods.greek.deity.god.Poseidon;
import com.censoredsoftware.demigods.greek.deity.god.Zeus;
import com.censoredsoftware.demigods.greek.deity.titan.Oceanus;
import com.censoredsoftware.demigods.greek.deity.titan.Perses;
import com.censoredsoftware.demigods.greek.item.armor.BootsOfPagos;
import com.censoredsoftware.demigods.greek.item.armor.FaultyBootsOfHermes;
import com.censoredsoftware.demigods.greek.item.book.BookOfPrayer;
import com.censoredsoftware.demigods.greek.item.book.WelcomeBook;
import com.censoredsoftware.demigods.greek.item.weapon.BowOfTria;
import com.censoredsoftware.demigods.greek.structure.Altar;
import com.censoredsoftware.demigods.greek.structure.Obelisk;
import com.censoredsoftware.demigods.greek.structure.Shrine;
import com.censoredsoftware.demigods.greek.trigger.NewPlayerNeedsHelp;
import com.censoredsoftware.demigods.greek.trigger.ProcessAltars;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
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
	{}

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
		return new String[] {};
	}

	@Override
	public Boolean useBaseGame()
	{
		return true;
	}

	/**
	 * Books
	 */
	public static final BookOfPrayer BOOK_OF_PRAYER = new BookOfPrayer();
	public static final WelcomeBook WELCOME_BOOK = new WelcomeBook();

	/**
	 * Weapons
	 */
	public static final BowOfTria BOW_OF_TRIA = new BowOfTria();

	/**
	 * Armor
	 */
	public static final BootsOfPagos BOOTS_OF_PAGOS = new BootsOfPagos();
	public static final FaultyBootsOfHermes FAULTY_BOOTS_OF_HERMES = new FaultyBootsOfHermes();

	@Override
	public ImmutableCollection<DivineItem> getDivineItems()
	{
		return ImmutableSet.of((DivineItem) BOOK_OF_PRAYER, WELCOME_BOOK, BOW_OF_TRIA, BOOTS_OF_PAGOS, FAULTY_BOOTS_OF_HERMES);
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.copyOf((Alliance[]) GreekAlliance.values());
	}

	/*
	 * Gods
	 */
	public static final Zeus ZEUS = new Zeus();
	public static final Poseidon POSEIDON = new Poseidon();

	/*
	 * Titans
	 */
	public static final Perses PERSES = new Perses();
	public static final Oceanus OCEANUS = new Oceanus();

	/*
	 * Fates
	 */
	public static final Clotho CLOTHO = new Clotho();
	public static final Lachesis LACHESIS = new Lachesis();
	public static final Atropos ATROPOS = new Atropos();

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.of((Deity) ZEUS, POSEIDON, PERSES, OCEANUS, CLOTHO, LACHESIS, ATROPOS);
	}

	public static final Altar ALTAR = new Altar();
	public static final Obelisk OBELISK = new Obelisk();
	public static final Shrine SHRINE = new Shrine();

	@Override
	public ImmutableCollection<Structure> getStructures()
	{
		return ImmutableSet.of((Structure) ALTAR, OBELISK, SHRINE);
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