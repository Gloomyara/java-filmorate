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
    public Film getById(Integer id) {
        if (filmStorage.containsKey(id)) {
            return filmStorage.get(id);
        }
        return null;
    }

    @Override
    public void put(Integer integer, Film film) {
        filmStorage.put(integer, film);
    }
}
