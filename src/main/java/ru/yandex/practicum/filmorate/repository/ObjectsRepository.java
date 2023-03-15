package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectsRepository<T> {
    protected final Map<Integer, T> objects = new HashMap<>();

    public Collection<T> findAll() {
        return objects.values();
    }

    public Map<Integer, T> get() {
        return objects;
    }
}
