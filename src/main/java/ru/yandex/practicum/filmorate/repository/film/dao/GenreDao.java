package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

import java.util.List;

public interface GenreDao extends GenreRepository {
    void saveFilmGenres(Long filmId, List<Genre> genres);

    void deleteAllFilmGenres(Long id);
}
