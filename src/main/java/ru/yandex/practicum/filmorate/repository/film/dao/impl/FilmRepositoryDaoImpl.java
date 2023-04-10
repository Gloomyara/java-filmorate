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
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.dao.FilmRepositoryDao;
import ru.yandex.practicum.filmorate.repository.film.dao.GenreRepositoryDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository("FilmRepositoryDao")
@Primary
public class FilmRepositoryDaoImpl implements FilmRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepositoryDao<Integer> genreRepository;

    @Override
    public void containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select id from films where id = ?", k);
        if (!filmRows.next()) {
            log.warn("{} with Id: {} not found",
                    "Film", k);
            throw new ObjectNotFoundException("Film with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select f.id, f.title, f.description, f.release_date, " +
                "f.length, r.id rating_id, r.name mpa, f.rate from films as f " +
                "left join ratings as r on r.id=f.rating_id";
        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Optional<Film> getByKey(Integer k) {
        try {
            String sqlQuery = "select f.id, f.title, f.description, f.release_date, " +
                    "f.length, r.id rating_id, r.name mpa, f.rate from films as f " +
                    "left join ratings as r on r.id=f.rating_id where f.id = ?";
            Optional<Film> optV = Optional.ofNullable(
                    jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, k));
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Film", k
            );
            return optV;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Film with Id: {} not found", k);
            return Optional.empty();
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
            stmt.setString(1, v.getName());
            stmt.setString(2, v.getDescription());
            stmt.setDate(3, Date.valueOf(v.getReleaseDate()));
            stmt.setInt(4, v.getDuration());
            stmt.setInt(5, v.getMpa().getId());
            return stmt;
        }, keyHolder);
        Integer k = Objects.requireNonNull(keyHolder.getKey()).intValue();
        v.setId(k);
        if (v.getGenres() != null && (v.getGenres().size() > 0)) {
            for (Genre g : v.getGenres()) {
                String sqlQuery1 = "insert into film_genre(film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery1, k, g.getId());
            }
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
        jdbcTemplate.update(sqlQuery,
                v.getName(),
                v.getDescription(),
                Date.valueOf(v.getReleaseDate()),
                v.getDuration(),
                v.getMpa().getId(),
                k);
        String sqlQuery1 = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery1, k);
        if (v.getGenres() != null && (v.getGenres().size() > 0)) {
            for (Genre g : v.getGenres()) {
                String sqlQuery2 = "insert into film_genre(film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery2, k, g.getId());
            }
        }
        return v;
    }

    @Override
    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        Film v = getByKey(k1).orElseThrow(
                () -> new ObjectNotFoundException("Film with Id: " + k1 + " not found")
        );
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
    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        Film v = getByKey(k1).orElseThrow(
                () -> new ObjectNotFoundException("Film with Id: " + k1 + " not found")
        );
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
        String sqlQuery = "select f.id, f.title, f.description, f.release_date, " +
                "f.length, r.id rating_id, r.name mpa, f.rate from films as f " +
                "left join ratings as r on r.id=f.rating_id order by f.rate desc limit ?";

        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, i);
        log.debug(
                "Запрос списка самых популярных {}'s успешно выполнен, всего {}: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        String sqlQuery = "select genre_id, from film_genre where film_id = ? order by genre_id";
        List<Integer> tempList = jdbcTemplate.query(sqlQuery,
                (rs, rw) -> rs.getInt("genre_id"),
                resultSet.getInt("id"));
        List<Genre> genreList = new ArrayList<>();
        if (!tempList.isEmpty()) {
            genreList = tempList.stream()
                    .map(genreRepository::getByKey)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("length"))
                .mpa(new Rating(resultSet.getInt("rating_id"), resultSet.getString("mpa")))
                .genres(genreList)
                .rate(resultSet.getInt("rate"))
                .build();
    }
}
