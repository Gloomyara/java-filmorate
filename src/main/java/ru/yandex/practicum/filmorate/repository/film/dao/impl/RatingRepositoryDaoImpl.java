package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.dao.RatingRepositoryDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RatingRepositoryDaoImpl implements RatingRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select id from ratings where id = ?", k);
        if (filmRows.next()) {
            return true;
        }
        log.warn("{} with Id: {} not found",
                "Rating", k);
        throw new ObjectNotFoundException("Rating with Id: " + k + " not found");
    }

    @Override
    public Collection<Rating> findAll() {
        String sqlQuery = "select id, name, description from genres";
        Collection<Rating> collection = jdbcTemplate.query(sqlQuery, this::mapRowToRating);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Rating", "Rating", collection.size()
        );
        return collection;
    }

    @Override
    public Rating getByKey(Integer k) throws ObjectNotFoundException {
        try {
            String sqlQuery = "select id, name, description from ratings where id = ?";
            Rating v = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, k))
                    .orElseThrow(
                            () -> new ObjectNotFoundException("Rating with Id: " + k + " not found")
                    );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Rating", k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Rating create(Rating rating) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Rating put(Rating rating) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {

        return Rating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
