package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements ObjectService<Integer, Film> {
    private final FilmRepository<Integer> repository;

    @Override
    public Collection<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film getByKey(Integer k) {
        return repository.getByKey(k);
    }

    @Override
    public Film create(Film v) {
        return repository.create(v);
    }

    @Override
    public Film put(Film v) {
        return repository.put(v);
    }

    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        return repository.addLike(k1, k2);
    }

    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        return repository.deleteLike(k1, k2);
    }

    public Collection<Film> getPopularFilms(Integer limit) {
        return repository.getPopularFilms(limit);
    }
}
