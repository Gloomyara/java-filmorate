package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.repository.film.DirectorRepository;

import java.util.List;

public interface DirectorDao extends DirectorRepository {
    void saveFilmDirector(Long filmId, List<Director> directors);

    void deleteAllFilmDirectors(Long id);
}
