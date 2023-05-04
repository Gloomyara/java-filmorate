package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;
import ru.yandex.practicum.filmorate.repository.EntityMapper;
import ru.yandex.practicum.filmorate.repository.film.dao.GenreDao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDaoImpl extends AbstractRepository<Genre> implements GenreDao {


    @Override
    public Optional<Genre> delete(Long id) {
        return Optional.empty();
    }

    protected GenreDaoImpl(JdbcTemplate jdbcTemplate, EntityMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void saveFilmGenres(Long filmId, List<Genre> genres) {
        if (!genres.isEmpty()) {
            genres.forEach(genre -> containsOrElseThrow(genre.getId()));
            deleteAllFilmGenres(filmId);
            jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                    genres,
                    100,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genre.getId());
                    });
        } else {
            deleteAllFilmGenres(filmId);
        }
    }

    @Override
    public void deleteAllFilmGenres(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID=?", id);
    }
}
