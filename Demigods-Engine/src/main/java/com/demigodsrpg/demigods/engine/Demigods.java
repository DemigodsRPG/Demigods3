package com.demigodsrpg.demigods.engine;

import com.demigodsrpg.demigods.engine.data.DemigodsWorld;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.mythos.Mythos;
import org.bukkit.conversations.ConversationFactory;

import java.util.Collection;

/**
 * Utility class for all of Demigods.
 */
public class Demigods {
    // -- CONSTANTS -- //
    private static final DemigodsServer DEMIGODS_SERVER = new DemigodsServer();
    private static final ConversationFactory CONVERSATION_FACTORY = new ConversationFactory(DemigodsPlugin.getInst());

    // -- CONSTRUCTOR -- //

    private Demigods() {
    }

    // -- GETTERS FOR OTHER MANAGERS/HANDLERS/HOLDERS -- //

    public static DemigodsServer getServer() {
        return DEMIGODS_SERVER;
    }

    public static ConversationFactory getConversationFactory() {
        return CONVERSATION_FACTORY;
    }

    public static Mythos getMythos() {
        return getServer().getMythos();
    }

    // -- PASS UP DATA FROM DEMIGODS SERVER CLASS -- //

    public static Collection<DemigodsPlayer> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }

    public static Collection<DemigodsCharacter> getOnlineCharacters() {
        return getServer().getOnlineCharacters();
    }

    public static DemigodsWorld getWorld(String name) {
        return getServer().getWorld(name);
    }

    public static Collection<DemigodsWorld> getWorlds() {
        return getServer().getWorlds();
    }
}
