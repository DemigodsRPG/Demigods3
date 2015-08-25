package com.demigodsrpg.demigods.base.command;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.helper.CommandManager;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

// TODO Convert this over to the sub-command format.

public class DevelopmentCommands extends CommandManager {
    @Override
    public ImmutableSet<String> getCommandNames() {
        return ImmutableSet.of("obelisk", "test1", "test2", "test3");
    }

    @Override
    public ImmutableList<Sub> getSubCommands() {
        return ImmutableList.of();
    }

    @Override
    public boolean always(CommandSender sender, Command command, String label, String[] args) {
        if ("test1".equalsIgnoreCase(command.getName())) return !test1(sender, args);
        if ("test2".equalsIgnoreCase(command.getName())) return !test2(sender, args);
        if ("test3".equalsIgnoreCase(command.getName())) return !test3(sender, args);
        return !("obelisk".equalsIgnoreCase(command.getName()) && obelisk(sender, args));
    }

    private static boolean test1(CommandSender sender, final String[] args) {
        final Player player = (Player) sender;

        final UUID id = DemigodsPlayer.of(player).getMojangAccount();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(DemigodsPlugin.getInst(), new Runnable() {
            @Override
            public void run() {
                OfflinePlayer offline = Bukkit.getServer().getOfflinePlayer(id);

                player.sendMessage(offline.isOnline() ? "Success!" : "Failure.");
            }
        });

        return true;
    }

    private static boolean test2(CommandSender sender, final String[] args) {
        Player player = (Player) sender;

        return true;
    }

    private static boolean test3(CommandSender sender, final String[] args) {
        Player player = (Player) sender;

        return true;
    }

    /**
     * Temp command while testing obelisks.
     */
    private static boolean obelisk(CommandSender sender, final String[] args) {
        Player player = (Player) sender;

        if (args.length != 3) {
            player.sendMessage(ChatColor.RED + "Not enough arguments.");
            return false;
        }

        DemigodsStructure obelisk = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(player.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
        if (obelisk != null) {
            DemigodsCharacter character = DemigodsCharacter.of(player);
            if (!obelisk.getOwner().equals(character.getId())) {
                player.sendMessage(ChatColor.RED + "You don't control this Obelisk.");
                return true;
            }

            DemigodsCharacter workWith = obeliskGetCharacter(args[1], args[2]);

            if (workWith == null) {
                player.sendMessage(ChatColor.RED + "Character/Player (" + args[2] + ") not found.");
                return true;
            }

            if (!workWith.alliedTo(character)) {
                player.sendMessage(ChatColor.RED + "You are not allied with " + workWith.getDeity().getColor() + character.getName() + ChatColor.RED + ".");
                return true;
            }

            if ("add".equalsIgnoreCase(args[0])) {
                if (!obelisk.getSanctifiers().contains(workWith.getId())) {
                    obelisk.addSanctifier(workWith.getId());
                    player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been added to the Obelisk!");
                } else player.sendMessage(ChatColor.RED + "Already a member.");
            } else if ("remove".equalsIgnoreCase(args[0])) {
                if (obelisk.getSanctifiers().contains(workWith.getId())) {
                    obelisk.removeSanctifier(workWith.getId());
                    player.sendMessage(workWith.getDeity().getColor() + workWith.getName() + ChatColor.YELLOW + " has been removed from the Obelisk!");
                } else player.sendMessage(ChatColor.RED + "Not a member.");
            }
        } else player.sendMessage(ChatColor.RED + "No Obelisk found.");

        return true;
    }

    private static DemigodsCharacter obeliskGetCharacter(String type, final String name) {
        if ("character".equalsIgnoreCase(type)) return Demigods.getServer().getCharacter(name);
        if (!"player".equalsIgnoreCase(type)) return null;
        try {
            return DemigodsPlayer.getFromName(name).getCharacter();
        } catch (Exception ignored) {
            // ignored
        }
        return null;
    }
}
