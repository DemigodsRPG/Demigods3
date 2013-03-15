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

package com.censoredsoftware.Demigods.API;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demigods;

public class AdminAPI
{
	private static final Demigods API = Demigods.INSTANCE;

	/*
	 * wandEnabled() : Returns true if the (OfflinePlayer)player's Demigods debugging is enabled.
	 */
	public boolean wandEnabled(OfflinePlayer player)
	{
		return API.misc.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && API.data.hasPlayerData(player, "temp_admin_wand") && API.data.getPlayerData(player, "temp_admin_wand").equals(true);
	}

	/*
	 * useWand() : Returns true if the (OfflinePlayer)player's admin wand is enabled and wielded.
	 */
	public boolean useWand(OfflinePlayer player)
	{
		return wandEnabled(player) && player.getPlayer().getItemInHand().getTypeId() == API.config.getSettingInt("admin_wand_tool");
	}

	/*
	 * debugEnabled() : Returns true if the (OfflinePlayer)player's Demigods debugging is enabled.
	 */
	public boolean playerDebugEnabled(OfflinePlayer player)
	{
		return API.misc.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && API.data.hasPlayerData(player, "temp_admin_debug") && API.data.getPlayerData(player, "temp_admin_debug").equals(true);
	}

	/*
	 * consoleDebugEnabled() : Returns true if console debug is enabled in the config.
	 */
	public static boolean consoleDebugEnabled()
	{
		return API.config.getSettingBoolean("console_debug");
	}

	/*
	 * sendDebug() : Sends a debug message to all applicable recipients.
	 */
	public void sendDebug(String message)
	{
		// Log to console
		if(consoleDebugEnabled()) API.misc.info("[Debug] " + ChatColor.stripColor(message));

		// Log to online, debugging admins
		for(Player player : API.player.getOnlineAdmins())
		{
			if(playerDebugEnabled(player)) player.sendMessage(ChatColor.RED + "[Debug] " + message);
		}
	}
}
