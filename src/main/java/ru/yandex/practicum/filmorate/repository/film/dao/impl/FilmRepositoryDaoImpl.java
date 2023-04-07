package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.dao.FilmRepositoryDao;

import java.util.Collection;

public class FilmRepositoryDaoImpl implements FilmRepositoryDao<Integer> {

    @Override
    public Collection<Film> findAll() {
        return null;
    }

    @Override
    public Film getByKey(Integer k) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Film put(Film film) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Film addLike(Integer k1, Integer k2) {
        return null;
    }

    @Override
    public Film deleteLike(Integer k1, Integer k2) {
        return null;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer i) {
        return null;
    }
}
