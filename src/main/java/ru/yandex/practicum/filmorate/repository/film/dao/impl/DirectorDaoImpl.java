package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;
import ru.yandex.practicum.filmorate.repository.EntityMapper;
import ru.yandex.practicum.filmorate.repository.film.dao.DirectorDao;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class DirectorDaoImpl extends AbstractRepository<Director> implements DirectorDao {

    protected DirectorDaoImpl(JdbcTemplate jdbcTemplate, EntityMapper<Director> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void saveFilmDirector(Long filmId, List<Director> directors) {
        if (!directors.isEmpty()) {
            directors.forEach(director -> containsOrElseThrow(director.getId()));
            deleteAllFilmDirectors(filmId);
            jdbcTemplate.batchUpdate("INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (?, ?)",
                    directors,
                    100,
                    (PreparedStatement ps, Director director) -> {
                        ps.setLong(1, filmId);
                        ps.setLong(2, director.getId());
                    });
        } else {
            deleteAllFilmDirectors(filmId);
        }
    }

    @Override
    public void deleteAllFilmDirectors(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_DIRECTOR WHERE FILM_ID=?", id);
    }

}