package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.dao.GenreRepositoryDao;

import java.util.Collection;

public class GenreRepositoryDaoImpl implements GenreRepositoryDao<Integer> {
    @Override
    public Collection<Genre> findAll() {
        return null;
    }

    @Override
    public Genre getByKey(Integer k) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Genre create(Genre genre) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Genre put(Genre genre) throws ObjectNotFoundException {
        return null;
    }
}
