package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.dao.GenreRepositoryDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GenreRepositoryDaoImpl implements GenreRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select id from genres where id = ?", k);
        if (filmRows.next()) {
            return true;
        }
        log.warn("{} with Id: {} not found",
                "Genre", k);
        throw new ObjectNotFoundException("Genre with Id: " + k + " not found");
    }

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "select id, name, description from genres";
        Collection<Genre> collection = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Genre", "Genre", collection.size()
        );
        return collection;
    }

    @Override
    public Genre getByKey(Integer k) throws ObjectNotFoundException {
        try {
            String sqlQuery = "select id, name, description from genres where id = ?";
            Genre v = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, k))
                    .orElseThrow(
                            () -> new ObjectNotFoundException("Genre with Id: " + k + " not found")
                    );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Genre", k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Genre create(Genre genre) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Genre put(Genre genre) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {

        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
