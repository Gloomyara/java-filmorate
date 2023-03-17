package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFilmRepository implements ObjectsRepository<Integer, Film> {
    protected final Map<Integer, Film> filmObjects = new HashMap<>();
    @Override
    public Collection<Film> findAll() {
        return filmObjects.values();
    }

    @Override
    public Map<Integer, Film> get() {
        return filmObjects;
    }
}
