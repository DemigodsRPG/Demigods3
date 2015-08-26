package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.conversation.Prayer;
import com.demigodsrpg.demigods.engine.data.TempDataManager;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Notification;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        try {
            DemigodsPlayer.of(player);
        } catch (Exception whoops) {
            player.kickPlayer("Error finding your Mojang Id, please try again.");
            Messages.warning(player.getName() + " could not join the game due to a Mojang Id problem.");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

        // Define variables
        Player player = event.getPlayer();
        DemigodsPlayer wrapper = DemigodsPlayer.of(player);
        DemigodsCharacter character = wrapper.getCharacter();

        // Set their last login-time
        Long now = System.currentTimeMillis();
        wrapper.setLastLoginTime(now);

        // Welcome back message
        Long time = wrapper.getLastLogoutTime();
        if (time != null && TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS) > 1) {
            Notification welcomeBack = Notification.create(Notification.SenderType.SERVER,
                    character, Notification.Alert.NEUTRAL, "Welcome back!",
                    "Lots has happened while you were away. You should check back more often!");
            wrapper.sendNotification(welcomeBack);
        }

        // Check for changed names
        if (!player.getName().equals(wrapper.getPlayerName())) wrapper.setPlayerName(player.getName());

        // Set display name
        if (character != null && wrapper.canUseCurrent()) {
            String name = character.getName();
            ChatColor color = character.getDeity().getColor();
            player.setDisplayName(color + name);
            player.setPlayerListName(color + name);
            if (character.isAlive()) {
                event.getPlayer().setMaxHealth(character.getMaxHealth());
                event.getPlayer().setHealth(character.getHealth() >= character.getMaxHealth() ? character.getMaxHealth() : character.getHealth());
            }
        }

        // Demigods welcome message
        if (Configs.getSettingBoolean("misc.welcome_message")) {
            player.sendMessage(English.RUNNING_DG_VERSION.getLine().replace("{version}", DemigodsPlugin.getInst().getDescription().getVersion()));
            player.sendMessage(English.DG_FOR_MORE_INFORMATION.getLine());
        }

        // Notifications
        if (character != null && character.getMeta().hasNotifications()) {
            int size = character.getMeta().getNotifications().size();
            player.sendMessage(size == 1 ? English.UNREAD_NOTIFICATION.getLine() : English.UNREAD_NOTIFICATIONS.getLine().replace("{size}", "" + size));
            player.sendMessage(English.FIND_ALTAR_TO_VIEW_NOTIFICATIONS.getLine());
        }

        // Remove temp battle data
        if (TempDataManager.hasKeyTemp(player.getName(), "quit_during_battle")) {
            TempDataManager.removeTemp(player.getName(), "quit_during_battle");
            player.sendMessage(English.WELCOME_BACK_IN_BATTLE.getLine());
        }

        // Alert of losing battle due to leaving
        if (TempDataManager.hasKeyTemp(player.getName(), "quit_during_battle_final")) {
            TempDataManager.removeTemp(player.getName(), "quit_during_battle_final");
            player.sendMessage(English.WELCOME_BACK_BATTLE_LOST.getLine());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Zones.inNoDemigodsZone(event.getPlayer().getLocation()) && Prayer.Util.isPraying(event.getPlayer()))
            Prayer.Util.togglePraying(event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Set their last logout-time
        Long now = System.currentTimeMillis();
        DemigodsPlayer.of(event.getPlayer()).setLastLogoutTime(now);

        // Everything else
        final String name = event.getPlayer().getName();
        if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
        final DemigodsCharacter loggingOff = DemigodsCharacter.of(event.getPlayer());
        if (loggingOff != null) {
            loggingOff.setLocation(event.getPlayer().getLocation());
            if (Battle.isInBattle(loggingOff)) {
                Battle battle = Battle.getBattle(loggingOff);
                battle.addDeath(loggingOff);
                TempDataManager.saveTemp(name, "quit_during_battle", true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!loggingOff.getBukkitOfflinePlayer().isOnline() && TempDataManager.hasKeyTemp(name, "quit_during_battle")) {
                            Battle battle = Battle.getBattle(loggingOff);
                            battle.removeParticipant(loggingOff);
                            TempDataManager.removeTemp(name, "quit_during_battle");
                            battle.sendMessage(English.DISCONNECT_DURING_BATTLE.getLine().replace("{name}", loggingOff.getName()));
                            TempDataManager.saveTemp(name, "quit_during_battle_final", true);
                        }
                    }
                }, 200);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
        DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
        if (character != null) {
            character.setAlive(true);
            double maxHealth = character.getMaxHealth();
            event.getPlayer().setMaxHealth(maxHealth);
            event.getPlayer().setHealth(maxHealth);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
        DemigodsCharacter character = DemigodsCharacter.of(event.getEntity());
        if (character != null) character.setAlive(false);
    }
}
