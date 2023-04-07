package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.dao.RatingRepositoryDao;

import java.util.Collection;

public class RatingRepositoryDaoImpl implements RatingRepositoryDao<Integer> {
    @Override
    public Collection<Rating> findAll() {
        return null;
    }

    @Override
    public Rating getByKey(Integer k) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Rating create(Rating rating) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Rating put(Rating rating) throws ObjectNotFoundException {
        return null;
    }
}
