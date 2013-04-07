/*
	Copyright (c) 2013 The Demigods Team
	
	Demigods License v1
	
	This plugin is provided "as is" and without any warranty.  Any express or
	implied warranties, including, but not limited to, the implied warranties
	of merchantability and fitness for a particular purpose are disclaimed.
	In no event shall the authors be liable to any party for any direct,
	indirect, incidental, special, exemplary, or consequential damages arising
	in any way out of the use or misuse of this plugin.

	Definitions

	 1. This Plugin is defined as all of the files within any archive
	    file or any group of files released in conjunction by the Demigods Team,
	    the Demigods Team, or a derived or modified work based on such files.

	 2. A Modification, or a Mod, is defined as this Plugin or a derivative of
	    it with one or more Modification applied to it, or as any program that
	    depends on this Plugin.

	 3. Distribution is defined as allowing one or more other people to in
	    any way download or receive a copy of this Plugin, a Modified
	    Plugin, or a derivative of this Plugin.

	 4. The Software is defined as an installed copy of this Plugin, a
	    Modified Plugin, or a derivative of this Plugin.

	 5. The Demigods Team is defined as Alex Bennett and Alexander Chauncey
	    of http://www.censoredsoftware.com/.
	
	Agreement
	
	 1. Permission is hereby granted to use, copy, modify and/or
	    distribute this Plugin, provided that:
	
	    a. All copyright notices within source files and as generated by
	       the Software as output are retained, unchanged.
	
	    b. Any Distribution of this Plugin, whether as a Modified Plugin
	       or not, includes this license and is released under the terms
	       of this Agreement. This clause is not dependant upon any
	       measure of changes made to this Plugin.
	
	    c. This Plugin, Modified Plugins, and derivative works may not
	       be sold or released under any paid license without explicit 
	       permission from the Demigods Team. Copying fees for the 
	       transport of this Plugin, support fees for installation or
	       other services, and hosting fees for hosting the Software may,
	       however, be imposed.
	
	    d. Any Distribution of this Plugin, whether as a Modified
	       Plugin or not, requires express written consent from the
	       Demigods Team.
	
	 2. You may make Modifications to this Plugin or a derivative of it,
	    and distribute your Modifications in a form that is separate from
	    the Plugin. The following restrictions apply to this type of
	    Modification:
	
	    a. A Modification must not alter or remove any copyright notices
	       in the Software or Plugin, generated or otherwise.
	
	    b. When a Modification to the Plugin is released, a
	       non-exclusive royalty-free right is granted to the Demigods Team
	       to distribute the Modification in future versions of the
	       Plugin provided such versions remain available under the
	       terms of this Agreement in addition to any other license(s) of
	       the initial developer.
	
	    c. Any Distribution of a Modified Plugin or derivative requires
	       express written consent from the Demigods Team.
	
	 3. Permission is hereby also granted to distribute programs which
	    depend on this Plugin, provided that you do not distribute any
	    Modified Plugin without express written consent.
	
	 4. The Demigods Team reserves the right to change the terms of this
	    Agreement at any time, although those changes are not retroactive
	    to past releases, unless redefining the Demigods Team. Failure to
	    receive notification of a change does not make those changes invalid.
	    A current copy of this Agreement can be found included with the Plugin.
	
	 5. This Agreement will terminate automatically if you fail to comply
	    with the limitations described herein. Upon termination, you must
	    destroy all copies of this Plugin, the Software, and any
	    derivatives within 48 hours.
 */

package com.censoredsoftware.Demigods.Theogony.Handlers;

import java.io.IOException;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Libraries.Metrics;
import com.censoredsoftware.Demigods.Libraries.Metrics.Graph;
import com.censoredsoftware.Demigods.Theogony.Theogony;

public class DMetricsHandler
{
	private static final Demigods API = Theogony.INSTANCE;
	private static Theogony instance;

	public DMetricsHandler(Theogony plugin)
	{
		instance = plugin;
	}

	public static void report()
	{
		try
		{
			Metrics metrics = new Metrics(instance);

			// Battles Graph
			Graph battles = metrics.createGraph("Battles");
			battles.addPlotter(new Metrics.Plotter("Active")
			{
				@Override
				public int getValue()
				{
					return API.battle.getAllActive().size();
				}
			});
			battles.addPlotter(new Metrics.Plotter("Total")
			{
				@Override
				public int getValue()
				{
					return API.data.getAllBattles().size();
				}
			});

			// Characters Graph
			Graph characters = metrics.createGraph("Characters");
			characters.addPlotter(new Metrics.Plotter("Active")
			{
				@Override
				public int getValue()
				{
					return API.character.getAllActive().size();
				}
			});
			characters.addPlotter(new Metrics.Plotter("Total")
			{
				@Override
				public int getValue()
				{
					return API.data.getAllChars().size();
				}
			});

			// Characters Per Alliance Graph
			Graph alliances = metrics.createGraph("Characters per Alliance");
			for(final String alliance : API.deity.getLoadedDeityAlliances())
			{
				alliances.addPlotter(new Metrics.Plotter(alliance + " Active")
				{
					@Override
					public int getValue()
					{
						return API.character.getActiveAllianceList(alliance).size();
					}
				});
				alliances.addPlotter(new Metrics.Plotter(alliance + " Total")
				{
					@Override
					public int getValue()
					{
						return API.character.getAllianceList(alliance).size();
					}
				});
			}

			// Characters Per Deity Graph
			Graph deities = metrics.createGraph("Characters per Deity");
			for(final String deity : API.deity.getAllDeities())
			{
				deities.addPlotter(new Metrics.Plotter(String.valueOf(deity.charAt(1)).toUpperCase() + deity.substring(1) + " Active")
				{
					@Override
					public int getValue()
					{
						return API.character.getActiveDeityList(deity).size();
					}
				});
				deities.addPlotter(new Metrics.Plotter(String.valueOf(deity.charAt(1)).toUpperCase() + deity.substring(1) + " Total")
				{
					@Override
					public int getValue()
					{
						return API.character.getDeityList(deity).size();
					}
				});
			}

			// Shrines Graph
			Graph shrines = metrics.createGraph("Shrines");
			shrines.addPlotter(new Metrics.Plotter("Total")
			{
				@Override
				public int getValue()
				{
					return API.block.getAllShrines().size();
				}
			});

			// Altars Graph
			Graph altars = metrics.createGraph("Altars");
			altars.addPlotter(new Metrics.Plotter("Total")
			{
				@Override
				public int getValue()
				{
					return API.block.getAllAltars().size();
				}
			});

			metrics.start();
		}
		catch(IOException e)
		{
			API.misc.severe(e.getMessage());
		}
	}
}
