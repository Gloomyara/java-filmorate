package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.FilmLike;
import ru.yandex.practicum.filmorate.repository.film.mapper.FilmLikesMapper;
import ru.yandex.practicum.filmorate.repository.film.dao.FilmLikesDao;

@Slf4j
@Component
public class FilmLikesDaoImpl implements FilmLikesDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmLikesMapper mapper;

    public FilmLikesDaoImpl(JdbcTemplate jdbcTemplate, FilmLikesMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public boolean addLike(long filmId, long userId, int rate) {
        try {
            new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(mapper.getTableName())
                    .execute(
                            mapper.toMap(FilmLike.builder()
                                    .filmId(filmId)
                                    .userId(userId)
                                    .rate(rate)
                                    .isPositive(rate > 5)
                                    .build())
                    );
            return true;
        } catch (DuplicateKeyException e) {
            log.warn(
                    "Error! Cannot add user Id: {} like." +
                            " User like already registered for Film Id: {}.",
                    userId, filmId
            );
            return false;
        }
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {

        String sqlQuery = "delete from user_film_like where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }
}