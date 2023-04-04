package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFilmRepository implements FilmRepository<Integer> {
    private final Map<Integer, Film> filmStorage = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return filmStorage.values();
    }

    @Override
    public Film getByKey(Integer k) {
        if (filmStorage.containsKey(k)) {
            return filmStorage.get(k);
        }
        return null;
    }

    @Override
    public void put(Integer k, Film v) {
        filmStorage.put(k, v);
    }
}
