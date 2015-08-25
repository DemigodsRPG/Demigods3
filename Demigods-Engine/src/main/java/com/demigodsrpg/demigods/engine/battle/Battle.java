package com.demigodsrpg.demigods.engine.battle;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.DemigodsServer;
import com.demigodsrpg.demigods.engine.data.*;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.DemigodsTameable;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.engine.event.BattleDeathEvent;
import com.demigodsrpg.demigods.engine.exception.SpigotNotFoundException;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.language.Symbol;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.util.*;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Battle extends DataAccess<UUID, Battle> {
    private UUID id;
    private String world;
    private UUID startLoc;
    private boolean active;
    private long startTime;
    private long deleteTime;
    private Set<String> involvedPlayers;
    private Set<String> involvedTameable;
    private int killCounter;
    private int runnableId;
    private Map<String, Object> kills;
    private Map<String, Object> deaths;
    private UUID startedBy;
    private Scoreboard scoreboard;

    private Battle(Object ignored) {
    }

    public Battle() {
        this.kills = Maps.newHashMap();
        this.deaths = Maps.newHashMap();
        this.involvedPlayers = Sets.newHashSet();
        this.involvedTameable = Sets.newHashSet();
        this.killCounter = 0;
    }

    @Register(idType = IdType.UUID)
    public Battle(UUID id, ConfigurationSection conf) {
        this.id = id;
        world = conf.getString("world");
        startLoc = UUID.fromString(conf.getString("startLoc"));
        active = conf.getBoolean("active");
        startTime = conf.getLong("startTime");
        deleteTime = conf.getLong("deleteTime");
        involvedPlayers = Sets.newHashSet(conf.getStringList("involvedPlayers"));
        involvedTameable = Sets.newHashSet(conf.getStringList("involvedTameable"));
        killCounter = conf.getInt("killCounter");
        runnableId = conf.getInt("runnableId");
        kills = conf.getConfigurationSection("kills").getValues(false);
        deaths = conf.getConfigurationSection("deaths").getValues(false);
        startedBy = UUID.fromString(conf.getString("startedBy"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", world);
        map.put("startLoc", startLoc.toString());
        map.put("active", active);
        map.put("startTime", startTime);
        map.put("deleteTime", deleteTime);
        map.put("involvedPlayers", Lists.newArrayList(involvedPlayers));
        map.put("involvedTameable", Lists.newArrayList(involvedTameable));
        map.put("killCounter", killCounter);
        map.put("runnableId", runnableId);
        map.put("kills", kills);
        map.put("deaths", deaths);
        map.put("startedBy", startedBy.toString());
        return map;
    }

    public void generateId() {
        id = UUID.randomUUID();
    }

    public void setActive() {
        this.active = true;
        save();
    }

    public void setInactive() {
        this.active = false;
        save();
    }

    void setStartLocation(Location location) {
        this.startLoc = DemigodsLocation.track(location).getId();
        this.world = location.getWorld().getName();
    }

    void setStartTime(long time) {
        this.startTime = time;
    }

    void setDeleteTime(long time) {
        this.deleteTime = time;
        save();
    }

    public UUID getId() {
        return this.id;
    }

    public double getRadius() {
        int base = Configs.getSettingInt("battles.min_radius");
        if (involvedPlayers.size() > 2) return base * Math.log10(10 + Math.ceil(Math.pow(involvedPlayers.size(), 1.5)));
        return base;
    }

    public boolean isActive() {
        return this.active;
    }

    public long getDuration() {
        long base = Configs.getSettingInt("battles.min_duration") * 1000;
        long per = Configs.getSettingInt("battles.duration_multiplier") * 1000;
        if (involvedPlayers.size() > 2) return base + (per * (involvedPlayers.size() - 2));
        return base;
    }

    public int getMinKills() {
        int base = Configs.getSettingInt("battles.min_kills");
        int per = 2;
        if (involvedPlayers.size() > 2) return base + (per * (involvedPlayers.size() - 2));
        return base;
    }

    public int getMaxKills() {
        int base = Configs.getSettingInt("battles.max_kills");
        int per = 3;
        if (involvedPlayers.size() > 2) return base + (per * (involvedPlayers.size() - 2));
        return base;
    }

    public DemigodsLocation getStartLocation() {
        return DemigodsLocation.get(WorldDataManager.getWorld(world), this.startLoc);
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getDeleteTime() {
        return this.deleteTime;
    }

    void setStarter(DemigodsCharacter character) {
        this.startedBy = character.getId();
        addParticipant(character);
    }

    public void addParticipant(Participant participant) {
        if (participant instanceof DemigodsCharacter) this.involvedPlayers.add((participant.getId().toString()));
        else this.involvedTameable.add(participant.getId().toString());
        save();
    }

    public void removeParticipant(Participant participant) {
        if (participant instanceof DemigodsCharacter) this.involvedPlayers.remove((participant.getId().toString()));
        else this.involvedTameable.remove(participant.getId().toString());
        save();
    }

    public void addKill(Participant participant) {
        this.killCounter += 1;
        DemigodsCharacter character = participant.getRelatedCharacter();
        if (this.kills.containsKey(character.getId().toString()))
            this.kills.put(character.getId().toString(), Integer.parseInt(this.kills.get(character.getId().toString()).
                    toString()) + 1);
        else this.kills.put(character.getId().toString(), 1);
        save();
    }

    public void addDeath(Participant participant) {
        DemigodsCharacter character = participant.getRelatedCharacter();
        if (this.deaths.containsKey(character.getId().toString()))
            this.deaths.put(character.getId().toString(), Integer.parseInt(this.deaths.get(character.getId().toString()).
                    toString()) + 1);
        else this.deaths.put(character.getId().toString(), 1);
        save();
    }

    public DemigodsCharacter getStarter() {
        return DemigodsCharacter.get(startedBy);
    }

    public Set<Participant> getParticipants() {
        return Sets.filter(Sets.union(Sets.newHashSet(Collections2.transform(involvedPlayers, character ->
                        DemigodsCharacter.get(UUID.fromString(character)))), Sets.newHashSet(Collections2.
                        transform(involvedTameable, tamable -> DemigodsTameable.get(UUID.fromString(tamable))))),
                participant -> participant != null && participant.getRelatedCharacter() != null);
    }

    public Collection<Alliance> getInvolvedAlliances() {
        Set<Alliance> set = Sets.newHashSet();
        set.addAll(getParticipants().stream().map(participant -> participant.getRelatedCharacter().getAlliance()).
                collect(Collectors.toList()));
        return set;
    }

    public int getKills(Participant participant) {
        try {
            return Integer.parseInt(kills.get(participant.getId().toString()).toString());
        } catch (Exception ignored) {
            // ignored
        }
        return 0;
    }

    public int getDeaths(Participant participant) {
        try {
            return Integer.parseInt(deaths.get(participant.getId().toString()).toString());
        } catch (Exception ignored) {
            // ignored
        }
        return 0;
    }

    public Map<UUID, Integer> getScores() {
        Map<UUID, Integer> score = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : kills.entrySet()) {
            if (!getParticipants().contains(DemigodsCharacter.get(UUID.fromString(entry.getKey())))) continue;
            score.put(UUID.fromString(entry.getKey()), Integer.parseInt(entry.getValue().toString()));
        }
        for (Map.Entry<String, Object> entry : deaths.entrySet()) {
            int base = 0;
            if (score.containsKey(UUID.fromString(entry.getKey()))) base = score.get(UUID.fromString(entry.getKey()));
            score.put(UUID.fromString(entry.getKey()), base - Integer.parseInt(entry.getValue().toString()));
        }
        return score;
    }

    public int getScore(final Alliance alliance) {
        Map<UUID, Integer> score = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : kills.entrySet()) {
            if (!getParticipants().contains(DemigodsCharacter.get(UUID.fromString(entry.getKey())))) continue;
            score.put(UUID.fromString(entry.getKey()), Integer.parseInt(entry.getValue().toString()));
        }
        for (Map.Entry<String, Object> entry : deaths.entrySet()) {
            int base = 0;
            if (score.containsKey(UUID.fromString(entry.getKey()))) base = score.get(UUID.fromString(entry.getKey()));
            score.put(UUID.fromString(entry.getKey()), base - Integer.parseInt(entry.getValue().toString()));
        }
        int sum = 0;
        for (int i : score.entrySet().stream().filter(entry ->
                DemigodsCharacter.get(entry.getKey()).getAlliance().getName().equalsIgnoreCase(alliance.getName())).map(Map.Entry::getValue).collect(Collectors.toList()))
            sum += i;
        return sum;
    }

    public Collection<DemigodsCharacter> getMVPs() {
        final int max = Collections.max(getScores().values());
        return Collections2.transform(Collections2.filter(getScores().entrySet(), entry -> entry.getValue() == max),
                entry -> DemigodsCharacter.get(entry.getKey()));
    }

    public int getKillCounter() {
        return this.killCounter;
    }

    public void end() // TODO Make this specify that it was a pet that won/lost a duel
    {
        for (String stringId : involvedPlayers)
            TimedServerData.saveTimed(stringId, "just_finished_battle", true, 1, TimeUnit.MINUTES);

        Map<UUID, Integer> scores = getScores();
        List<UUID> participants = Lists.newArrayList(scores.keySet());
        if (participants.size() == 2) {
            if (scores.get(participants.get(0)).equals(scores.get(participants.get(1)))) {
                DemigodsCharacter one = DemigodsCharacter.get(participants.get(0));
                DemigodsCharacter two = DemigodsCharacter.get(participants.get(1));
                Messages.broadcast(one.getDeity().getColor() + one.getName() + ChatColor.GRAY + " and " + two.getDeity().getColor() + two.getName() + ChatColor.GRAY + " just tied in a duel.");
            } else {
                int winnerIndex = scores.get(participants.get(0)) > scores.get(participants.get(1)) ? 0 : 1;
                DemigodsCharacter winner = DemigodsCharacter.get(participants.get(winnerIndex));
                DemigodsCharacter loser = DemigodsCharacter.get(participants.get(winnerIndex == 0 ? 1 : 0));
                Messages.broadcast(winner.getDeity().getColor() + winner.getName() + ChatColor.GRAY + " just won in a duel against " + loser.getDeity().getColor() + loser.getName() + ChatColor.GRAY + ".");
            }
        } else if (participants.size() > 2) {
            Alliance winningAlliance = null;
            int winningScore = 0;
            Collection<DemigodsCharacter> MVPs = getMVPs();
            boolean oneMVP = MVPs.size() == 1;
            for (Alliance alliance : getInvolvedAlliances()) {
                int score = getScore(alliance);
                if (getScore(alliance) > winningScore) {
                    winningAlliance = alliance;
                    winningScore = score;
                }
            }
            if (winningAlliance != null) {
                Messages.broadcast(ChatColor.GRAY + "The " + ChatColor.YELLOW + winningAlliance.getName() + "s " + ChatColor.GRAY + "just won a battle involving " + involvedPlayers.size() + " participants.");
                Messages.broadcast(ChatColor.GRAY + "The " + ChatColor.YELLOW + "MVP" + (oneMVP ? "" : "s") + ChatColor.GRAY + " from this battle " + (oneMVP ? "is" : "are") + ":");
                for (DemigodsCharacter mvp : MVPs)
                    Messages.broadcast(" " + ChatColor.DARK_GRAY + Symbol.RIGHTWARD_ARROW + " " + mvp.getDeity().getColor() + mvp.getName() + ChatColor.GRAY + " / " + ChatColor.YELLOW + "Kills" + ChatColor.GRAY + ": " + getKills(mvp) + " / " + ChatColor.YELLOW + "Deaths" + ChatColor.GRAY + ": " + getDeaths(mvp));
            }
        }

        // Reset scoreboards
        resetScoreboards();

        // Remind of cooldown
        sendMessage(ChatColor.YELLOW + "You are safe for 60 seconds.");

        // Prepare for graceful delete
        setDeleteTime(System.currentTimeMillis() + 3000L);
        setInactive();
    }

    public void sendMessage(String message) {
        for (String stringId : involvedPlayers) {
            OfflinePlayer offlinePlayer = DemigodsCharacter.get(UUID.fromString(stringId)).getBukkitOfflinePlayer();
            if (offlinePlayer.isOnline()) offlinePlayer.getPlayer().sendMessage(message);
        }
    }

    public void startScoreboardRunnable() {
        final Battle battle = this;

        runnableId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.getInst(), () -> {
            // FIXME This loop LAGS THE HECK OUT OF EVERYTHING
            updateScoreboard(battle);
            for (String stringId : involvedPlayers) {
                OfflinePlayer offlinePlayer = DemigodsCharacter.get(UUID.fromString(stringId)).getBukkitOfflinePlayer();
                if (offlinePlayer.isOnline()) offlinePlayer.getPlayer().setScoreboard(battle.scoreboard);
            }
        }, 20, 20);
    }

    public void resetScoreboards() {
        // Cancel the runnable
        Bukkit.getScheduler().cancelTask(runnableId);

        // Clear the scoreboards
        for (String stringId : involvedPlayers) {
            OfflinePlayer offlinePlayer = DemigodsCharacter.get(UUID.fromString(stringId)).getBukkitOfflinePlayer();
            if (offlinePlayer.isOnline())
                offlinePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    // -- STATIC GETTERS/SETTERS -- //

    private static final DataAccess<UUID, Battle> DATA_ACCESS = new Battle(null);

    public static Battle get(UUID id) {
        return DATA_ACCESS.getDirect(id);
    }

    public static Collection<Battle> all() {
        return DATA_ACCESS.allDirect();
    }

    // -- UTIL METHODS -- //

    public static Battle create(Participant damager, Participant damaged) {
        Battle battle = new Battle();
        battle.generateId();
        battle.setStartLocation(damager.getCurrentLocation().toVector().getMidpoint(damaged.getCurrentLocation().toVector()).toLocation(damager.getCurrentLocation().getWorld()));
        battle.setStartTime(System.currentTimeMillis());
        battle.setActive();
        battle.setStarter(damager.getRelatedCharacter());
        battle.addParticipant(damager);
        battle.addParticipant(damaged);
        battle.startScoreboardRunnable();
        battle.save();

        // Log the creation
        Messages.info(English.LOG_BATTLE_STARTED.getLine().replace("{locX}", battle.getStartLocation().getX() + "").replace("{locY}", battle.getStartLocation().getY() + "").replace("{locZ}", battle.getStartLocation().getZ() + "").replace("{world}", battle.getStartLocation().getWorld().getName()).replace("{starter}", battle.getStarter().getName()));

        return battle;
    }

    public static List<Battle> getAllActive() {
        return all().stream().filter(Battle::isActive).collect(Collectors.toList());
    }

    public static List<Battle> getAllInactive() {
        return all().stream().filter(b -> !b.isActive()).collect(Collectors.toList());
    }

    public static boolean existsInRadius(Location location) {
        return getInRadius(location) != null;
    }

    public static Battle getInRadius(final Location location) {
        try {
            return getAllActive().stream().filter(battle -> battle.getStartLocation().distance(location)
                    <= battle.getRadius()).findAny().get();
        } catch (Exception ignored) {
            // ignored
        }
        return null;
    }

    public static boolean isInBattle(final Participant participant) {
        return getAllActive().stream().anyMatch(battle -> battle.getParticipants().contains(participant));
    }

    public static Battle getBattle(final Participant participant) {
        try {
            return getAllActive().stream().filter(battle -> battle.getParticipants().contains(participant)).findAny()
                    .get();
        } catch (Exception ignored) {
            // ignored
        }
        return null;
    }

    public static boolean existsNear(Location location) {
        return getNear(location) != null;
    }

    public static Battle getNear(final Location location) {
        try {
            return getAllActive().stream().filter(battle -> {
                    double distance = battle.getStartLocation().distance(location);
                    return distance > battle.getRadius() && distance <= Configs.getSettingInt("battles.merge_radius");
            }).findAny().get();
        } catch (Exception ignored) {
            // ignored
        }
        return null;
    }

    public static List<Location> battleBorder(final Battle battle) {
        if (!DemigodsServer.isRunningSpigot()) throw new SpigotNotFoundException();
        return DemigodsLocation.getCirclePoints(battle.getStartLocation().getBukkitLocation(), battle.getRadius(), 120).
                stream().map(point -> new Location(point.getWorld(), point.getBlockX(), point.getWorld().
                getHighestBlockYAt(point), point.getBlockZ())).collect(Collectors.toList());
    }

    /*
     * This is completely broken. TODO
     */
    public static Location randomRespawnPoint(Battle battle) {
        List<Location> respawnPoints = getSafeRespawnPoints(battle);
        if (respawnPoints.size() == 0) return battle.getStartLocation().getBukkitLocation();

        Location target = respawnPoints.get(Randoms.generateIntRange(0, respawnPoints.size() - 1));

        Vector direction = target.toVector().subtract(battle.getStartLocation().getBukkitLocation().toVector()).normalize();
        double X = direction.getX();
        double Y = direction.getY();
        double Z = direction.getZ();

        // Now change the angle FIXME
        Location changed = target.clone();
        changed.setYaw(180 - DemigodsLocation.toDegree(Math.atan2(Y, X)));
        changed.setPitch(90 - DemigodsLocation.toDegree(Math.acos(Z)));
        return changed;
    }

    /*
     * This is completely broken. TODO
     */
    public static boolean isSafeLocation(Location reference, Location checking) {
        if (checking.getBlock().getType().isSolid() || checking.getBlock().getType().equals(Material.LAVA))
            return false;
        double referenceY = reference.getY();
        double checkingY = checking.getY();
        return Math.abs(referenceY - checkingY) <= 5;
    }

    public static List<Location> getSafeRespawnPoints(final Battle battle) {
        return Lists.newArrayList(Collections2.filter(Collections2.transform(DemigodsLocation.getCirclePoints(battle.getStartLocation().getBukkitLocation(), battle.getRadius() - 1.5, 100), new Function<Location, Location>() {
            @Override
            public Location apply(Location point) {
                return new Location(point.getWorld(), point.getBlockX(), point.getWorld().getHighestBlockYAt(point), point.getBlockZ());
            }
        }), location -> isSafeLocation(battle.getStartLocation().getBukkitLocation(), location)));
    }

    public static boolean canParticipate(Entity entity) {
        if (entity instanceof Player) {
            DemigodsCharacter character = DemigodsCharacter.of((Player) entity);
            return character != null && !character.getDeity().getFlags().contains(Deity.Flag.NO_BATTLE);
        }
        return entity instanceof Tameable && DemigodsTameable.of((LivingEntity) entity) != null && isInBattle(DemigodsTameable.of((LivingEntity) entity).getRelatedCharacter());
    }

    public static Participant defineParticipant(Entity entity) {
        if (!canParticipate(entity)) return null;
        if (entity instanceof Player) return DemigodsCharacter.of((Player) entity);
        return DemigodsTameable.of((LivingEntity) entity);
    }

    public static void battleDeath(Participant damager, Participant damagee, Battle battle) {
        BattleDeathEvent event = new BattleDeathEvent(battle, damagee, damager);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        if (damager instanceof DemigodsCharacter) ((DemigodsCharacter) damager).addKill();
        if (damager.getRelatedCharacter().getBukkitOfflinePlayer().isOnline())
            damager.getRelatedCharacter().getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "+1 Kill.");
        battle.addKill(damager);
        damagee.getEntity().setHealth(damagee.getEntity().getMaxHealth());
        Vehicles.teleport(damagee.getEntity(), randomRespawnPoint(battle));
        if (damagee instanceof DemigodsCharacter) {
            DemigodsCharacter character = (DemigodsCharacter) damagee;
            Player player = character.getBukkitOfflinePlayer().getPlayer();
            player.setFoodLevel(20);
            for (PotionEffect potionEffect : player.getActivePotionEffects())
                player.removePotionEffect(potionEffect.getType());
            character.setPotionEffects(player.getActivePotionEffects());
            character.addDeath(damager.getRelatedCharacter());
        }
        if (damagee.getRelatedCharacter().getBukkitOfflinePlayer().isOnline())
            damagee.getRelatedCharacter().getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "+1 Death.");
        battle.addDeath(damagee);
    }

    public static void battleDeath(Participant damagee, Battle battle) {
        BattleDeathEvent event = new BattleDeathEvent(battle, damagee);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        damagee.getEntity().setHealth(damagee.getEntity().getMaxHealth());
        damagee.getEntity().teleport(randomRespawnPoint(battle));
        if (damagee instanceof DemigodsCharacter) ((DemigodsCharacter) damagee).addDeath();
        if (damagee.getRelatedCharacter().getBukkitOfflinePlayer().isOnline())
            damagee.getRelatedCharacter().getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "+1 Death.");
        battle.addDeath(damagee);
    }

    public static boolean canTarget(Entity entity) {
        return !canParticipate(entity) || canParticipate(entity) && canTarget(defineParticipant(entity));
    }

    /**
     * Returns true if target is allowed for <code>player</code>.
     *
     * @param participant the player to check.
     * @return true/false depending on if target is allowed.
     */
    public static boolean canTarget(Participant participant) // TODO REDO THIS
    {
        return participant == null || participant.canPvp() || participant.getCurrentLocation() != null && !Zones.inNoPvpZone(participant.getCurrentLocation());
    }

    /**
     * Updates all battle particles. Meant for use in a Runnable.
     */
    public static void updateBattleParticles() {
        for (Battle battle : getAllActive())
            for (Location point : battleBorder(battle))
                point.getWorld().playEffect(point, Effect.MOBSPAWNER_FLAMES, 0, (int) (battle.getRadius() * 2));
    }

    /**
     * Updates all battles.
     */
    public static void updateBattles() {
        // End all active battles that should end.
        getAllActive().stream().filter(battle -> battle.getKillCounter() >= battle.getMaxKills() || battle.getStartTime() + battle.getDuration()
                <= System.currentTimeMillis() && battle.getKillCounter() >= battle.getMinKills() || battle.getParticipants().size() < 2 || battle.getInvolvedAlliances().size() < 2).forEach(battle -> {
            battle.end();
            Skill.processBattle(battle);
        });

        // Delete all inactive battles that should be deleted.
        getAllInactive().stream().filter(battle -> battle.getDeleteTime() >= System.currentTimeMillis()).
                forEach(Battle::remove);
    }

    /**
     * Updates the scoreboard for the given <code>player</code> with information from the <code>battle</code>.
     *
     * @param battle the battle to grab stats from.
     */
    public static void updateScoreboard(Battle battle) {
        // Define sidebar objective
        Objective info;

        // Define scoreboard
        if (battle.scoreboard == null) {
            battle.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            info = battle.scoreboard.registerNewObjective("battle_info", "dummy");
        } else {
            info = battle.scoreboard.getObjective("battle_info");
        }

        // Define sidebar objective

        info.setDisplaySlot(DisplaySlot.SIDEBAR);
        info.setDisplayName(ChatColor.AQUA + "Current Battle Stats");

        // Add the information
        Score kills = info.getScore(ChatColor.GRAY + "Total Kills");
        kills.setScore(battle.getKillCounter());

        Score neededKills = info.getScore(ChatColor.GRAY + "Kills Needed");
        neededKills.setScore(battle.getMinKills());

        for (Alliance alliance : battle.getInvolvedAlliances()) {
            Score allianceKills = info.getScore(ChatColor.YELLOW + alliance.getName() + ChatColor.GRAY + " Score");
            allianceKills.setScore(battle.getScore(alliance));
        }

        Score participants = info.getScore(ChatColor.GRAY + "Participants");
        participants.setScore(battle.involvedPlayers.size());

        Score points = info.getScore(ChatColor.GRAY + "Duration");
        points.setScore((int) (System.currentTimeMillis() - battle.getStartTime()) / 1000);
    }
}
