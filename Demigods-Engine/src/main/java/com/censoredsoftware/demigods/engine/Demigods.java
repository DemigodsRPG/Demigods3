package com.censoredsoftware.demigods.engine;

import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.data.file.FileDataManager;
import com.censoredsoftware.demigods.engine.mythos.Mythos;
import org.bukkit.conversations.ConversationFactory;

/**
 * Utility class for all of Demigods.
 */
public class Demigods
{
	// -- CONSTANTS -- //
	private static final DemigodsServer DEMIGODS_SERVER = new DemigodsServer();
	private static final ConversationFactory CONVERSATION_FACTORY = new ConversationFactory(DemigodsPlugin.getInst());
	private static final DataManager DATA_MANAGER;

	// -- STATIC DATA CONSTRUCTOR -- //
	static
	{
		// Get the correct data manager.
		String saveMethod = DemigodsPlugin.getInst().getConfig().getString("saving.method", "file");
		switch(saveMethod.toLowerCase())
		{
			case "file":
			{
				DATA_MANAGER = new FileDataManager();
				break;
			}
			default:
			{
				DemigodsPlugin.getInst().getLogger().severe("\"" + saveMethod + "\" is not a valid save method.");
				DemigodsPlugin.getInst().getLogger().severe("Defaulting to file save method.");
				DATA_MANAGER = new FileDataManager();
				break;
			}
		}
	}

	// -- CONSTRUCTOR -- //

	private Demigods()
	{}

	// -- GETTERS FOR OTHER MANAGERS/HANDLERS/HOLDERS -- //

	public static DemigodsServer getServer()
	{
		return DEMIGODS_SERVER;
	}

	public static ConversationFactory getConversationFactory()
	{
		return CONVERSATION_FACTORY;
	}

	public static DataManager getDataManager()
	{
		return DATA_MANAGER;
	}

	public static Mythos getMythos()
	{
		return getServer().getMythos();
	}
}
