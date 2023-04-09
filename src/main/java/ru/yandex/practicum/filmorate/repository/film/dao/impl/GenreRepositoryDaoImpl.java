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
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.dao.GenreRepositoryDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository("GenreRepositoryDao")
@Primary
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
        String sqlQuery = "select id, name from genres";
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
            String sqlQuery = "select id, name from genres where id = ?";
            Genre v = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, k);
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Genre", k
            );
            return v;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Genre with Id: {} not found", k);
            throw new ObjectNotFoundException("Genre with Id: " + k + " not found");
        }
    }

    @Override
    public Genre create(Genre v) throws ObjectAlreadyExistException {
        if (v.getId() != null) {
            int i = v.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Genre", i
            );
            throw new ObjectAlreadyExistException("Genre Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        String sqlQuery = "insert into genres(name) values (?)";
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
                "Genre", k
        );
        return v;
    }

    @Override
    public Genre put(Genre v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        String sqlQuery = "update genres set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery, v.getName(), k);
        return v;
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {

        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
