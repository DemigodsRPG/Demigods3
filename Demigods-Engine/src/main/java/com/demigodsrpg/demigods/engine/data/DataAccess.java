package com.demigodsrpg.demigods.engine.data;

import com.demigodsrpg.demigods.engine.data.serializable.DataSerializable;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class DataAccess<K extends Comparable, V extends DataAccess<K, V>> implements DataSerializable {
    @SuppressWarnings("RedundantCast")
    private final Class clazz = getClass();

	/*
     * Access to Data Object Classes from Data Manager.
	 */

    protected abstract K getId();

	/*
	 * Direct access to Data Manager from Data Object Classes.
	 */

    public V getDirect(K key) {
        return (V) DataManager.DATA_MANAGER.getFor(clazz, key);
    }

    public Collection<V> allDirect() {
        return (Collection) DataManager.DATA_MANAGER.getAllOf(clazz);
    }

    public Collection<V> allDirectWith(Predicate<V> predicate) {
        return Collections2.filter(allDirect(), predicate);
    }

    public ConcurrentMap<K, V> mapDirect() {
        return DataManager.DATA_MANAGER.getMapFor(clazz);
    }

    public void putDirect(K key, V value) {
        DataManager.DATA_MANAGER.putFor(clazz, key, value);
    }

    public void removeDirect(K key) {
        DataManager.DATA_MANAGER.removeFor(clazz, key);
    }

	/*
	 * Convenience methods for Data Object Classes.
	 */

    public void save() {
        putDirect(getId(), (V) this);
    }

    public void remove() {
        removeDirect(getId());
    }
}
