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
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.dao.FilmRepositoryDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository("FilmRepositoryDao")
@Primary
public class FilmRepositoryDaoImpl implements FilmRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select id from films where id = ?", k);
        if (filmRows.next()) {
            return true;
        }
        log.warn("{} with Id: {} not found",
                "Film", k);
        throw new ObjectNotFoundException("Film with Id: " + k + " not found");
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select id, title, description, release_date, " +
                "length, rating_id, rate from films";
        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Film getByKey(Integer k) throws ObjectNotFoundException {
        try {
            String sqlQuery = "select id, title, description, release_date, " +
                    "length, rating_id, rate from films where id = ?";
            Film v = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, k);
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Film", k
            );
            return v;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Film with Id: {} not found", k);
            throw new ObjectNotFoundException("Film with Id: " + k + " not found");
        }
    }

    @Override
    public Film create(Film v) throws ObjectAlreadyExistException {
        if (v.getId() != null) {
            int i = v.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Film", i
            );
            throw new ObjectAlreadyExistException("Film Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        String sqlQuery = "insert into films(title, description, release_date, " +
                "length, rating_id) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, v.getTitle());
            stmt.setString(2, v.getDescription());
            stmt.setDate(3, Date.valueOf(v.getReleaseDate()));
            stmt.setInt(4, v.getLength());
            stmt.setInt(5, v.getRatingId());
            return stmt;
        }, keyHolder);
        Integer k = Objects.requireNonNull(keyHolder.getKey()).intValue();
        v.setId(k);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            String sqlQuery1 = "insert into film_genre(film_id, genre_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery1, k, i);
        }
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "Film", k
        );
        return v;
    }

    @Override
    public Film put(Film v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        String sqlQuery = "update films set " +
                "title = ?, description = ?, release_date = ?, length = ?, rating_id = ?" +
                " where id = ?";
        jdbcTemplate.update(sqlQuery
                , v.getTitle()
                , v.getDescription()
                , Date.valueOf(v.getReleaseDate())
                , v.getLength()
                , v.getRatingId()
                , k);
        String sqlQuery1 = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery1, k);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            String sqlQuery2 = "insert into film_genre(film_id, genre_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery2, k, i);
        }
        return v;
    }

    @Override
    public Film addLike(Integer k1, Integer k2) {
        Film v = getByKey(k1);
        SqlRowSet favoriteFilmsRows = jdbcTemplate.queryForRowSet(
                "select * from favorite_films " +
                        "where film_id = ? " +
                        "and user_id = ?", k1, k2);
        Integer rate = v.getRate();
        if (!favoriteFilmsRows.next()) {
            String sqlQuery = "insert into favorite_films(film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, k1, k2);
            rate = rate + 1;
            v.setRate(rate);
            String sqlQuery1 = "update films set rate = ? where id = ?";
            jdbcTemplate.update(sqlQuery1, rate, k1);
        }
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, rate
        );
        return v;
    }

    @Override
    public Film deleteLike(Integer k1, Integer k2) {
        Film v = getByKey(k1);
        String sqlQuery = "delete from favorite_films where film_id = ? and user_id = ?";
        boolean b1 = jdbcTemplate.update(sqlQuery, k1, k2) > 0;
        if (!b1) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    k2
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + k2 + " like, user like not found.");
        }
        Integer rate = v.getRate() - 1;
        String sqlQuery1 = "update films set rate = ? where id = ?";
        jdbcTemplate.update(sqlQuery1, rate, k1);
        v.setRate(rate);
        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, rate
        );
        return v;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer i) {
        String sqlQuery = "select id, title, description, release_date, " +
                "length, rating_id, rate from films order by rate desc limit ?";
        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, i);
        log.debug(
                "Запрос списка самых популярных {}'s успешно выполнен, всего {}: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        String sqlQuery = "select genre_id, from film_genre where film_id = ?";
        var tempSet = jdbcTemplate.queryForStream(
                        sqlQuery, (rs, rn) -> rs.getInt("genre_id"),
                        resultSet.getInt("id"))
                .collect(Collectors.toSet());

        return Film.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .length(resultSet.getInt("length"))
                .ratingId(resultSet.getInt("rating_id"))
                .genreIdSet(tempSet)
                .rate(resultSet.getInt("rate"))
                .build();
    }
}
