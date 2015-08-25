package com.demigodsrpg.demigods.engine.data;

import com.demigodsrpg.demigods.engine.data.file.FileDataManager;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public abstract class DataManager {
    // TODO Should we let people register these as a service, just like Mythos?

    static final DataManager DATA_MANAGER = findManager();

    private static DataManager findManager() {
        return trainManager(FileDataManager.class);
    }

    private static DataManager trainManager(Class<? extends DataManager> manager) {
        try {
            return manager.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static final WorldDataManager WORLD_DATA_MANAGER = new WorldDataManager();

    protected abstract boolean preInit();

    protected abstract void init();

    protected abstract void save();

    protected abstract void flushData() throws AccessDeniedException;

    protected abstract <K extends Comparable, V extends DataAccess<K, V>> V getFor(Class clazz, K key);

    protected abstract <K extends Comparable, V extends DataAccess<K, V>> Collection<DataAccess> getAllOf(Class clazz);

    protected abstract <K extends Comparable, V extends DataAccess<K, V>> ConcurrentMap<K, V> getMapFor(Class clazz);

    protected abstract <K extends Comparable, V extends DataAccess<K, V>> void putFor(Class clazz, K key, V value);

    protected abstract <K extends Comparable, V extends DataAccess<K, V>> void removeFor(Class clazz, K key);

    public static void initAllData() {
        WORLD_DATA_MANAGER.init();
        DATA_MANAGER.init();
    }

    public static void saveAllData() {
        WORLD_DATA_MANAGER.save();
        DATA_MANAGER.save();
    }

    public static void flushAllData() throws AccessDeniedException {
        WORLD_DATA_MANAGER.flushData();
        DATA_MANAGER.flushData();
    }
}
