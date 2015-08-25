package com.demigodsrpg.demigods.greek.structure;

import com.demigodsrpg.demigods.engine.conversation.Administration;
import com.demigodsrpg.demigods.engine.data.TimedServerData;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.schematic.BlockData;
import com.demigodsrpg.demigods.engine.schematic.Schematic;
import com.demigodsrpg.demigods.engine.schematic.Selection;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Colors;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.language.English;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Obelisk extends GreekStructureType {
    private static final String name = "Obelisk";
    private static final Function<Location, GreekStructureType.Design> getDesign = new Function<Location, DemigodsStructureType.Design>() {
        @Override
        public DemigodsStructureType.Design apply(Location reference) {
            switch (reference.getBlock().getBiome()) {
                case OCEAN:
                case BEACH:
                case DESERT:
                case DESERT_HILLS:
                    return ObeliskDesign.DESERT;
                case HELL:
                    return ObeliskDesign.NETHER;
                default:
                    return ObeliskDesign.GENERAL;
            }
        }
    };
    private static final Function<GreekStructureType.Design, DemigodsStructure> createNew = new Function<GreekStructureType.Design, DemigodsStructure>() {
        @Override
        public DemigodsStructure apply(GreekStructureType.Design design) {
            DemigodsStructure save = new DemigodsStructure();
            save.setSanctifiers(new HashMap<String, Long>());
            save.setCorruptors(new HashMap<String, Long>());
            return save;
        }
    };
    private static final DemigodsStructureType.InteractFunction<Boolean> sanctify = new DemigodsStructureType.InteractFunction<Boolean>() {
        @Override
        public Boolean apply(DemigodsStructure data, DemigodsCharacter character) {
            if (!character.alliedTo(DemigodsCharacter.get(data.getOwner())) || !data.getSanctifiers().contains(character.getId()))
                return false;
            Location location = data.getBukkitLocation();
            location.getWorld().playSound(location, Sound.CAT_PURR, 0.3f, 0.7F);
            MaterialData colorData = Colors.getMaterial(character.getDeity().getColor());
            location.getWorld().playEffect(location.clone().add(0, 1, 0), Effect.STEP_SOUND, colorData.getItemTypeId(), colorData.getData());
            return true;
        }
    };
    private static final DemigodsStructureType.InteractFunction<Boolean> corrupt = new DemigodsStructureType.InteractFunction<Boolean>() {
        @Override
        public Boolean apply(DemigodsStructure data, DemigodsCharacter character) {
            if (character.alliedTo(DemigodsCharacter.get(data.getOwner()))) return false;
            if (DemigodsCharacter.get(data.getOwner()) != null) {
                DemigodsPlayer demigodsPlayer = DemigodsPlayer.of(DemigodsCharacter.get(data.getOwner()).getBukkitOfflinePlayer());
                long lastLogoutTime = demigodsPlayer.getLastLogoutTime();
                Calendar calendarHalfHour = Calendar.getInstance();
                calendarHalfHour.add(Calendar.MINUTE, -30);
                long thirtyMinutesAgo = calendarHalfHour.getTime().getTime();
                Calendar calendarWeek = Calendar.getInstance();
                calendarWeek.add(Calendar.WEEK_OF_YEAR, -3);
                long threeWeeksAgo = calendarWeek.getTime().getTime();
                if (!demigodsPlayer.getBukkitOfflinePlayer().isOnline() && lastLogoutTime != -1 && (lastLogoutTime > System.currentTimeMillis() - thirtyMinutesAgo || lastLogoutTime < threeWeeksAgo)) {
                    character.getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW + "This obelisk currently immune to damage.");
                    return false;
                }
            }
            Location location = data.getBukkitLocation();
            location.getWorld().playSound(location, Sound.WITHER_HURT, 0.4F, 1.5F);
            for (Location found : data.getBukkitLocations())
                location.getWorld().playEffect(found, Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());

            character.getBukkitOfflinePlayer().getPlayer().sendMessage("Corruption: " + (data.getSanctity() - data.getCorruption()));

            return true;
        }
    };
    private static final DemigodsStructureType.InteractFunction<Boolean> birth = new DemigodsStructureType.InteractFunction<Boolean>() {
        @Override
        public Boolean apply(DemigodsStructure data, DemigodsCharacter character) {
            Location location = data.getBukkitLocation();
            location.getWorld().strikeLightningEffect(location);
            location.getWorld().strikeLightningEffect(character.getLocation());
            return true;
        }
    };
    private static final DemigodsStructureType.InteractFunction<Boolean> kill = new DemigodsStructureType.InteractFunction<Boolean>() {
        @Override
        public Boolean apply(DemigodsStructure data, DemigodsCharacter character) {
            Location location = data.getBukkitLocation();
            location.getWorld().playSound(location, Sound.WITHER_DEATH, 1F, 1.2F);
            location.getWorld().createExplosion(location, 2F, false);
            character.addKill();
            return true;
        }
    };
    private static final Set<DemigodsStructureType.Flag> flags = new HashSet<DemigodsStructureType.Flag>() {
        {
            add(DemigodsStructureType.Flag.DESTRUCT_ON_BREAK);
            add(DemigodsStructureType.Flag.NO_GRIEFING);
        }
    };
    private static final Listener listener = new Listener() {
        @EventHandler(priority = EventPriority.HIGH)
        public void createAndRemove(PlayerInteractEvent event) {
            if (event.getClickedBlock() == null) return;

            if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

            // Define variables
            Block clickedBlock = event.getClickedBlock();
            Location location = clickedBlock.getLocation();
            Player player = event.getPlayer();

            if (DemigodsPlayer.isImmortal(player)) {
                DemigodsCharacter character = DemigodsCharacter.of(player);

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !character.getDeity().getFlags().contains(Deity.Flag.NO_OBELISK) && character.getDeity().getClaimItems().keySet().contains(event.getPlayer().getItemInHand().getType()) && Util.validBlockConfiguration(event.getClickedBlock())) {
                    if (DemigodsStructureType.Util.noOverlapStructureNearby(location)) {
                        player.sendMessage(ChatColor.YELLOW + "This location is too close to a no-pvp zone, please try again.");
                        return;
                    }

                    try {
                        // Obelisk created!
                        Administration.Util.sendDebug(ChatColor.RED + "Obelisk created by " + character.getName() + " at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());
                        DemigodsStructure save = inst().createNew(true, null, location);
                        save.setOwner(character.getId());
                        inst().birth(save, character);

                        player.sendMessage(ChatColor.GRAY + English.NOTIFICATION_OBELISK_CREATED.getLine());
                        event.setCancelled(true);
                    } catch (Exception errored) {
                        // Creation of shrine failed...
                        Messages.warning(errored.getMessage());
                    }
                }
            }

            if (Administration.Util.useWand(player) && DemigodsStructureType.Util.partOfStructureWithType(location, "Obelisk")) {
                event.setCancelled(true);

                DemigodsStructure save = DemigodsStructureType.Util.getStructureRegional(location);
                DemigodsCharacter owner = DemigodsCharacter.get(save.getOwner());

                if (TimedServerData.exists(player.getName(), "destroy_obelisk")) {
                    // Remove the Obelisk
                    save.remove();
                    TimedServerData.remove(player.getName(), "destroy_obelisk");

                    Administration.Util.sendDebug(ChatColor.RED + "Obelisk owned by (" + owner.getName() + ") at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ() + " removed.");

                    player.sendMessage(ChatColor.GREEN + English.ADMIN_WAND_REMOVE_OBELISK_COMPLETE.getLine());
                } else {
                    TimedServerData.saveTimed(player.getName(), "destroy_obelisk", true, 5, TimeUnit.SECONDS);
                    player.sendMessage(ChatColor.RED + English.ADMIN_WAND_REMOVE_OBELISK.getLine());
                }
            }
        }
    };
    private static final int radius = Configs.getSettingInt("zones.obelisk_radius");
    private static final Predicate<Player> allow = new Predicate<Player>() {
        @Override
        public boolean apply(Player player) {
            return true;
        }
    };
    private static final float sanctity = 850F, sanctityRegen = 1F;

    private static final Schematic general = new Schematic("general", "HmmmQuestionMark", 3) {
        {
            // Everything else.
            add(new Selection(0, 0, -1, 0, 2, -1, BlockData.Preset.STONE_BRICK));
            add(new Selection(0, 0, 1, 0, 2, 1, BlockData.Preset.STONE_BRICK));
            add(new Selection(1, 0, 0, 1, 2, 0, BlockData.Preset.STONE_BRICK));
            add(new Selection(-1, 0, 0, -1, 2, 0, BlockData.Preset.STONE_BRICK));
            add(new Selection(0, 4, -1, 0, 5, -1, BlockData.Preset.STONE_BRICK));
            add(new Selection(0, 4, 1, 0, 5, 1, BlockData.Preset.STONE_BRICK));
            add(new Selection(1, 4, 0, 1, 5, 0, BlockData.Preset.STONE_BRICK));
            add(new Selection(-1, 4, 0, -1, 5, 0, BlockData.Preset.STONE_BRICK));
            add(new Selection(0, 0, 0, 0, 4, 0, Material.REDSTONE_BLOCK));
            add(new Selection(0, 3, -1, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 3, 1, Material.REDSTONE_LAMP_ON));
            add(new Selection(1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(-1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 5, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(1, 5, -1, BlockData.Preset.VINE_1));
            add(new Selection(-1, 5, -1, BlockData.Preset.VINE_1));
            add(new Selection(1, 5, 1, BlockData.Preset.VINE_4));
            add(new Selection(-1, 5, 1, BlockData.Preset.VINE_4));
        }
    };
    private static final Schematic desert = new Schematic("desert", "HmmmQuestionMark", 3) {
        {
            // Everything else.
            add(new Selection(0, 0, -1, 0, 2, -1, Material.SANDSTONE));
            add(new Selection(0, 0, 1, 0, 2, 1, Material.SANDSTONE));
            add(new Selection(1, 0, 0, 1, 2, 0, Material.SANDSTONE));
            add(new Selection(-1, 0, 0, -1, 2, 0, Material.SANDSTONE));
            add(new Selection(0, 4, -1, 0, 5, -1, Material.SANDSTONE));
            add(new Selection(0, 4, 1, 0, 5, 1, Material.SANDSTONE));
            add(new Selection(1, 4, 0, 1, 5, 0, Material.SANDSTONE));
            add(new Selection(-1, 4, 0, -1, 5, 0, Material.SANDSTONE));
            add(new Selection(0, 0, 0, 0, 4, 0, Material.REDSTONE_BLOCK));
            add(new Selection(0, 3, -1, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 3, 1, Material.REDSTONE_LAMP_ON));
            add(new Selection(1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(-1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 5, 0, Material.REDSTONE_LAMP_ON));
        }
    };
    private static final Schematic nether = new Schematic("nether", "HmmmQuestionMark", 3) {
        {
            // Everything else.
            add(new Selection(0, 0, -1, 0, 2, -1, Material.NETHER_BRICK));
            add(new Selection(0, 0, 1, 0, 2, 1, Material.NETHER_BRICK));
            add(new Selection(1, 0, 0, 1, 2, 0, Material.NETHER_BRICK));
            add(new Selection(-1, 0, 0, -1, 2, 0, Material.NETHER_BRICK));
            add(new Selection(0, 4, -1, 0, 5, -1, Material.NETHER_BRICK));
            add(new Selection(0, 4, 1, 0, 5, 1, Material.NETHER_BRICK));
            add(new Selection(1, 4, 0, 1, 5, 0, Material.NETHER_BRICK));
            add(new Selection(-1, 4, 0, -1, 5, 0, Material.NETHER_BRICK));
            add(new Selection(0, 0, 0, 0, 4, 0, Material.REDSTONE_BLOCK));
            add(new Selection(0, 3, -1, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 3, 1, Material.REDSTONE_LAMP_ON));
            add(new Selection(1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(-1, 3, 0, Material.REDSTONE_LAMP_ON));
            add(new Selection(0, 5, 0, Material.REDSTONE_LAMP_ON));
        }
    };

    private static final int generationPoints = 1;

    private Obelisk() {
        super(name, ObeliskDesign.values(), getDesign, createNew, sanctify, corrupt, birth, kill, flags, listener, radius, allow, sanctity, sanctityRegen, generationPoints);
    }

    public static enum ObeliskDesign implements GreekStructureType.Design {
        GENERAL("general", general), DESERT("desert", desert), NETHER("nether", nether);

        private final String name;
        private final Schematic schematic;

        private ObeliskDesign(String name, Schematic schematic) {
            this.name = name;
            this.schematic = schematic;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Set<Location> getClickableBlocks(Location reference) {
            return getSchematic(null).getLocations(reference);
        }

        @Override
        public Schematic getSchematic(DemigodsStructure unused) {
            return schematic;
        }
    }

    public static class Util {
        public static boolean validBlockConfiguration(Block block) {
            if (!block.getType().equals(Material.REDSTONE_BLOCK)) return false;
            if (!block.getRelative(1, 0, 0).getType().equals(Material.STONE)) return false;
            if (!block.getRelative(-1, 0, 0).getType().equals(Material.STONE)) return false;
            if (!block.getRelative(0, 0, 1).getType().equals(Material.STONE)) return false;
            return block.getRelative(0, 0, -1).getType().equals(Material.STONE) && !block.getRelative(1, 0, 1).getType().isSolid() && !block.getRelative(1, 0, -1).getType().isSolid() && !block.getRelative(-1, 0, 1).getType().isSolid() && !block.getRelative(-1, 0, -1).getType().isSolid();
        }
    }

    private static final DemigodsStructureType INST = new Obelisk();

    public static DemigodsStructureType inst() {
        return INST;
    }
}
