package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FilmRepositoryDao<K> extends FilmRepository<K> {
    Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException;
}
