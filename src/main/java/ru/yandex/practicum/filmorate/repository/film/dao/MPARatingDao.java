package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.repository.film.MPARatingRepository;

public interface MPARatingDao extends MPARatingRepository {
    void saveFilmMpa(Long filmId, Long mpaId);

    void deleteAllFilmMpa(Long id);
}
