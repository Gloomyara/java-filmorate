package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.dao.RatingRepositoryDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository("RatingRepositoryDao")
@Primary
public class RatingRepositoryDaoImpl implements RatingRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select id from ratings where id = ?", k);
        if (!filmRows.next()) {
            log.warn("{} with Id: {} not found",
                    "Rating", k);
            throw new ObjectNotFoundException("Rating with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<Rating> findAll() {
        String sqlQuery = "select id rating_id, name mpa from ratings";
        Collection<Rating> collection = jdbcTemplate.query(sqlQuery, this::mapRowToRating);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Rating", "Rating", collection.size()
        );
        return collection;
    }

    @Override
    public Optional<Rating> getByKey(Integer k) {
        try {
            String sqlQuery = "select id rating_id, name mpa from ratings where id = ?";
            Optional<Rating> v = Optional.ofNullable(
                    jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, k));
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Rating", k
            );
            return v;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Rating with Id: {} not found", k);
            return Optional.empty();
        }
    }

    @Override
    public Rating create(Rating v) throws ObjectAlreadyExistException {
        if (v.getId() != null) {
            int i = v.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Rating", i
            );
            throw new ObjectAlreadyExistException("Rating Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        String sqlQuery = "insert into ratings(name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, v.getName());
            return stmt;
        }, keyHolder);
        Integer k = Objects.requireNonNull(keyHolder.getKey()).intValue();
        v.setId(k);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "Rating", k
        );
        return v;
    }

    @Override
    public Rating put(Rating v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        String sqlQuery = "update ratings set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery, v.getName(), k);
        return v;
    }

    @Override
    public Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {

        return Rating.builder()
                .id(resultSet.getInt("rating_id"))
                .name(resultSet.getString("mpa"))
                .build();
    }
}
