package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
@Repository("GenreRepositoryDao")
@Primary
public class GenreRepositoryDaoImpl implements GenreRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "select id genre_id, name genre_name from genres";
        Collection<Genre> collection = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Genre", "Genre", collection.size()
        );
        return collection;
    }

    @Override
    public Optional<Genre> getByKey(Integer k) {
        try {
            String sqlQuery = "select id genre_id, name genre_name from genres where id = ?";
            Optional<Genre> v = Optional.ofNullable(
                    jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, k));
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Genre", k
            );
            return v;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Genre with Id: {} not found", k);
            return Optional.empty();
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
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("id");
        Integer k = simpleJdbcInsert.executeAndReturnKey(v.toMap()).intValue();
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
        String sqlQuery = "update genres set name = ? where id = ?";
        if (jdbcTemplate.update(sqlQuery, v.getName(), k) > 0) {
            log.debug(
                    "Данные {} по Id: {}, успешно обновлены.",
                    "Genre", k
            );
            return v;
        }
        log.warn("{} with Id: {} not found",
                "Genre", k);
        throw new ObjectNotFoundException("Genre with Id: " + k + " not found");
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {

        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
