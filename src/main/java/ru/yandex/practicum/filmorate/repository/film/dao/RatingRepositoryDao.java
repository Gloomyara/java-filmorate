package ru.yandex.practicum.filmorate.repository.film.dao;

import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RatingRepositoryDao<K> extends RatingRepository<K> {
    Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException;
}
