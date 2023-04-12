package ru.yandex.practicum.filmorate.repository.film.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.dao.FilmRepositoryDao;
import ru.yandex.practicum.filmorate.repository.film.dao.RatingRepositoryDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository("FilmRepositoryDao")
@Primary
public class FilmRepositoryDaoImpl implements FilmRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;
    private final RatingRepositoryDao<Integer> ratingRepository;

    private final String newBigSqlQuery = "select f.id, f.title, f.description, f.release_date, " +
            "f.length, r.id rating_id, r.name mpa, f.rate, " +
            "listagg (fg.genre_id, ',') within group (order by g.id) genre_id_list, " +
            "listagg (g.name, ',') within group (order by g.id) genre_name_list " +
            "from films as f " +
            "left join ratings as r on r.id = f.rating_id " +
            "left join film_genre as fg on fg.film_id = f.id " +
            "left join genres as g on g.id = fg.genre_id "; //+ " group by f.id"

    @Override
    public Collection<Film> findAll() {
        try {
            String sqlQuery = newBigSqlQuery + " group by f.id";
            Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
            log.debug(
                    "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                    "Film", "Film", collection.size()
            );
            return collection;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Film> getByKey(Integer k) {
        try {
            String sqlQuery = newBigSqlQuery +
                    " where f.id = ? group by f.id";

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
        Integer k;
        if (v.getId() != null) {
            int i = v.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Film", i
            );
            throw new ObjectAlreadyExistException("Film Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("films")
                    .usingGeneratedKeyColumns("id");
            k = simpleJdbcInsert.executeAndReturnKey(v.toMap()).intValue();
        } catch (DataIntegrityViolationException e) {
            log.warn("Ошибка при создании фильма Id: {}! " +
                    "Обнаружен не зарегистрированный рейтинг! " +
                    v.getId(), v.getMpa());
            throw new ObjectNotFoundException(
                    "Ошибка при создании фильма Id: " + v.getId() +
                            "! Обнаружен не зарегистрированный рейтинг! " +
                            v.getMpa());
        }
        try {
            if (v.getGenres() != null && (v.getGenres().size() > 0)) {
                List<Genre> genreIdSet = v.getGenres().stream()
                        .distinct().collect(Collectors.toList());
                v.setGenres(genreIdSet);
                for (Genre g : genreIdSet) {
                    String sqlQuery1 = "insert into film_genre(film_id, genre_id) " +
                            "values (?, ?)";
                    jdbcTemplate.update(sqlQuery1, k, g.getId());
                }
            }
            log.debug(
                    "{} под Id: {}, успешно зарегистрирован.",
                    "Film", v.getId()
            );
            v.setId(k);
            return v;
        } catch (DataIntegrityViolationException e) {
            log.warn("Ошибка при создании фильма Id: {}! " +
                    "В списке жанров, обнаружены не зарегистрированные жанры! " +
                    v.getId(), v.getGenres());
            throw new ObjectNotFoundException(
                    "Ошибка при создании фильма Id: " + v.getId() +
                            "! В списке жанров, обнаружены не зарегистрированные жанры! " +
                            v.getGenres());
        }
    }

    @Override
    public Film put(Film v) throws ObjectNotFoundException {
        boolean b;
        Integer k = v.getId();
        try {
            String sqlQuery = "update films set " +
                    "title = ?, description = ?, release_date = ?, length = ?, rating_id = ?" +
                    " where id = ?";
            b = jdbcTemplate.update(sqlQuery,
                    v.getName(),
                    v.getDescription(),
                    Date.valueOf(v.getReleaseDate()),
                    v.getDuration(),
                    v.getMpa().getId(),
                    k) > 0;
        } catch (DataIntegrityViolationException e) {
            log.warn("Ошибка при обновлении фильма Id: {}!" +
                    " Обнаружен не зарегистрированный рейтинг! " +
                    k, v.getMpa());
            throw new ObjectNotFoundException(
                    "Ошибка при обновлении фильма Id: " + k +
                            "! Обнаружен не зарегистрированный рейтинг! " +
                            v.getMpa());
        }
        if (b) {
            try {
                String sqlQuery1 = "delete from film_genre where film_id = ?";
                jdbcTemplate.update(sqlQuery1, k);
                if (v.getGenres() != null && (v.getGenres().size() > 0)) {
                    List<Genre> genreIdSet = v.getGenres().stream()
                            .distinct().collect(Collectors.toList());
                    v.setGenres(genreIdSet);
                    for (Genre g : genreIdSet) {
                        String sqlQuery2 = "insert into film_genre(film_id, genre_id) " +
                                "values (?, ?)";
                        jdbcTemplate.update(sqlQuery2, k, g.getId());
                    }
                }
            } catch (DataIntegrityViolationException e) {
                log.warn("Ошибка при обновлении фильма Id: {}! " +
                        "В списке жанров, обнаружены не зарегистрированные жанры! " +
                        k, v.getGenres());
                throw new ObjectNotFoundException(
                        "Ошибка при обновлении фильма Id: " + k +
                                "! В списке жанров, обнаружены не зарегистрированные жанры! " +
                                v.getGenres());
            }
            log.debug(
                    "Данные {} по Id: {}, успешно обновлены.",
                    "Film", k
            );
            return v;
        }
        log.warn("{} with Id: {} not found",
                "Film", k);
        throw new ObjectNotFoundException("Film with Id: " + k + " not found");
    }

    @Override
    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException, ObjectAlreadyExistException {
        try {
            Film v = getByKey(k1).orElseThrow(
                    () -> new ObjectNotFoundException("Film with Id: " + k1 + " not found")
            );
            Integer rate = v.getRate();
            String sqlQuery = "insert into favorite_films(film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, k1, k2);
            rate = rate + 1;
            v.setRate(rate);
            String sqlQuery1 = "update films set rate = ? where id = ?";
            jdbcTemplate.update(sqlQuery1, rate, k1);
            log.debug(
                    "Фильм под Id: {} получил лайк от пользователя" +
                            " с Id: {}. Всего лайков: {}.",
                    k1, k2, rate
            );
            return v;
        } catch (DuplicateKeyException e) {
            log.warn(
                    "Error! Cannot add user Id: {} like." +
                            " User like already registered for Film Id: {}.",
                    k2, k1
            );
            throw new ObjectAlreadyExistException("Error! Cannot add user Id: " + k2 + " like." +
                    " User like already registered for Film Id: " + k1);
        } catch (DataIntegrityViolationException e) {
            log.warn(
                    "Error! Cannot add user Id: {} like, user not found.",
                    k2
            );
            throw new ObjectNotFoundException("Error! Cannot add user Id: "
                    + k2 + " like, user not found.");
        }
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
                        " с Id: {}. Всего лайков: {}.",
                k1, k2, rate
        );
        return v;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer i) {
        String sqlQuery = newBigSqlQuery +
                " group by f.id order by f.rate desc limit ?";

        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, i);
        log.debug(
                "Запрос списка самых популярных {}'s успешно выполнен, всего {}: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film;
        Optional<Rating> optV = Optional.empty();
        if (resultSet.getInt("rating_id") > 0) {
            optV = Optional.of(ratingRepository.mapRowToRating(resultSet, rowNum));
        }
        film = Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("length"))
                .mpa(optV.orElse(null))
                .genres(new ArrayList<>())
                .rate(resultSet.getInt("rate"))
                .build();
        if (resultSet.getString("genre_id_list") != null) {
            String[] genreId = resultSet.getString("genre_id_list").split(",");
            String[] genreName = resultSet.getString("genre_name_list").split(",");
            for (int i = 0; i < genreId.length; i++) {
                film.getGenres().add(new Genre(Integer.parseInt(genreId[i]), genreName[i]));
            }
        }
        return film;
    }
}