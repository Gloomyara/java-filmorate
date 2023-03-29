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
    public Map<Integer, Film> getStorage() {
        return filmStorage;
    }

    @Override
    public Film getById(Integer id) {
        return filmStorage.get(id);
    }

    @Override
    public void put(Integer integer, Film film) {
        filmStorage.put(integer, film);
    }
}

