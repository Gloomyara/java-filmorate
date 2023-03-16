package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectsRepository<K, V> {
    protected final Map<K, V> objects = new HashMap<>();

    public Collection<V> findAll() {
        return objects.values();
    }

    public Map<K, V> get() {
        return objects;
    }
}
