package com.demigodsrpg.demigods.engine.data;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimedServerData extends DataAccess<UUID, TimedServerData> {
    private UUID id;
    private String key;
    private String subKey;
    private Object data;
    private long expiration;

    public TimedServerData() {
    }

    @Register(idType = IdType.UUID)
    public TimedServerData(UUID id, ConfigurationSection conf) {
        this.id = id;
        key = conf.getString("key");
        subKey = conf.getString("subKey");
        data = conf.get("data");
        expiration = conf.getLong("expiration");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("subKey", subKey);
        map.put("data", data);
        map.put("expiration", expiration);
        return map;
    }

    public void generateId() {
        id = UUID.randomUUID();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public void setData(Object data) {
        if (data instanceof String || data instanceof Integer || data instanceof Boolean || data instanceof Double || data instanceof Map || data instanceof List)
            this.data = data;
        else if (data == null) this.data = "null";
        else this.data = data.toString();
    }

    public void setMilliSeconds(long millis) {
        this.expiration = System.currentTimeMillis() + millis;
    }

    public UUID getId() {
        return this.id;
    }

    public String getKey() {
        return this.key;
    }

    public String getSubKey() {
        return this.subKey;
    }

    public Object getData() {
        return this.data;
    }

    public long getExpiration() {
        return this.expiration;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final TimedServerData other = (TimedServerData) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.key, other.key) && Objects.equal(this.subKey, other.subKey) && Objects.equal(this.data, other.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.key, this.subKey, this.data);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id).add("key", this.key).add("subkey", this.subKey).add("data", this.data).toString();
    }

    private static final DataAccess<UUID, TimedServerData> DATA_ACCESS = new TimedServerData();

    public static Collection<TimedServerData> all() {
        return DATA_ACCESS.allDirect();
    }

    /*
     * Timed data
     */
    public static void saveTimed(String key, String subKey, Object data, long time, TimeUnit unit) {
        // Remove the data if it exists already
        remove(key, subKey);

        // Create and save the timed data
        TimedServerData timedData = new TimedServerData();
        timedData.generateId();
        timedData.setKey(key);
        timedData.setSubKey(subKey);
        timedData.setData(data);
        timedData.setMilliSeconds(unit.toMillis(time));
        timedData.save();
    }

    public static boolean exists(String key, String subKey) {
        return find(key, subKey) != null;
    }

    public static Object value(String key, String subKey) {
        return find(key, subKey).getData();
    }

    public static long getExpiration(String key, String subKey) {
        return find(key, subKey).getExpiration();
    }

    public static TimedServerData find(String key, String subKey) {
        if (findByKey(key) == null) return null;

        for (TimedServerData data : findByKey(key))
            if (data.getSubKey().equals(subKey)) return data;

        return null;
    }

    public static Set<TimedServerData> findByKey(final String key) {
        return Sets.newHashSet(Collections2.filter(all(), new Predicate<TimedServerData>() {
            @Override
            public boolean apply(TimedServerData serverData) {
                return serverData.getKey().equals(key);
            }
        }));
    }

    public static void remove(String key, String subKey) {
        if (find(key, subKey) != null) find(key, subKey).remove();
    }

    /**
     * Clears all expired timed data.
     */
    public static void clearExpired() {
        for (TimedServerData data : Collections2.filter(all(), new Predicate<TimedServerData>() {
            @Override
            public boolean apply(TimedServerData data) {
                return data.getExpiration() <= System.currentTimeMillis();
            }
        }))
            data.remove();
    }
}
