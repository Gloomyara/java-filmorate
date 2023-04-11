package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GenreRepositoryDao<K> extends GenreRepository<K> {
    Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException;
}
