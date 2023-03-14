package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectsRepository<T> {
    private final Map<String, T> objects = new HashMap<>();
    public Collection<T> findAll(){
        return objects.values();
    }
    public Map<String, T> get(){
        return objects;
    }
}
