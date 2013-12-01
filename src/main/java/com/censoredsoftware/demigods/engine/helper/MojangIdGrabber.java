package com.censoredsoftware.demigods.engine.helper;

import com.censoredsoftware.demigods.engine.data.DataManager;
import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;
import org.bukkit.OfflinePlayer;

public class MojangIdGrabber {
    private static final String AGENT = "minecraft";
    private static HttpProfileRepository repository;

    public static void load() {
        repository = new HttpProfileRepository();
    }

    /**
     * This method requires an OfflinePlayer as an extra step to prevent just passing in random String names.
     *
     * @param player The offline player that we are checking the UUID of.
     * @return The Mojang UUID.
     */
    public static String getUUID(OfflinePlayer player) {
        // Get the player's name.
        String playerName = player.getName();

        // Check if we already know this Id, of if the name actually belongs to a player.
        if (DataManager.hasTimed(playerName, "mojangAccount"))
            return DataManager.getTimedValue(playerName, "mojangAccount").toString();
        if (DataManager.hasTimed(playerName, "fakePlayer")) return null;

        // Get the Id from Mojang.
        IdGetTask task = new IdGetTask(playerName);
        task.run();
        if (!task.getStatus()) {
            DataManager.saveTimed(playerName, "fakePlayer", true, 60);
            return null;
        }
        String id = task.getUUID();

        // Put the player in the known Ids, and return the found Id.
        DataManager.saveTimedWeek(playerName, "mojangAccount", id);
        return id;
    }

    private static class IdGetTask implements Runnable {
        private String playerName;
        private String playerId;
        private Boolean status;

        private IdGetTask(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public void run() {
            Profile[] profiles = repository.findProfilesByCriteria(new ProfileCriteria(playerName, AGENT));
            if (profiles.length == 1) {
                playerId = profiles[0].getId();
                status = true;
            } else status = false;
        }

        private String getUUID() {
            return playerId;
        }

        private Boolean getStatus() {
            return status;
        }
    }
}
