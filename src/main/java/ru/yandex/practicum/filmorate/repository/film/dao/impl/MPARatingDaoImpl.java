package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;
import ru.yandex.practicum.filmorate.repository.EntityMapper;
import ru.yandex.practicum.filmorate.repository.film.dao.MPARatingDao;

import java.util.Optional;

@Component
public class MPARatingDaoImpl extends AbstractRepository<MPARating> implements MPARatingDao {

    @Override
    public Optional<MPARating> delete(Long id) {
        return Optional.empty();
    }

    protected MPARatingDaoImpl(JdbcTemplate jdbcTemplate, EntityMapper<MPARating> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void saveFilmMpa(Long filmId, Long mpaId) {
        if (mpaId != null) {
            containsOrElseThrow(mpaId);
            deleteAllFilmMpa(filmId);
            jdbcTemplate.update("INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES (?, ?)", filmId, mpaId);
        } else {
            deleteAllFilmMpa(filmId);
        }
    }

    @Override
    public void deleteAllFilmMpa(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_MPA WHERE FILM_ID=?", id);
    }
}
