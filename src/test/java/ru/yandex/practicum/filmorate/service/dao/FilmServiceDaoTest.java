package ru.yandex.practicum.filmorate.service.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.MethodNotAllowedException;
import ru.yandex.practicum.filmorate.controller.film.GenreController;
import ru.yandex.practicum.filmorate.controller.film.RatingController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceDaoTest {
    private final FilmService filmService;
    private final UserService userService;
    private final GenreController genreController;
    private final RatingController ratingController;
    private final JdbcTemplate jdbcTemplate;
    Film film;
    Film film1;
    User user;
    List<Genre> genreIdSet;
    Rating mpa;

    @BeforeEach
    void createSomeData() {
        genreIdSet = new ArrayList<>();
        genreIdSet.add(genreController.getByKey(1));
        mpa = ratingController.getByKey(1);
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 1500, mpa, genreIdSet, 0);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
    }

    @AfterEach
    void deleteSomeData() {

        String sqlQuery1 = "delete from film_genre";
        jdbcTemplate.update(sqlQuery1);
        String sqlQuery2 = "delete from favorite_films";
        jdbcTemplate.update(sqlQuery2);
        String sqlQuery3 = "delete from films";
        jdbcTemplate.update(sqlQuery3);
        String sqlQuery4 = "delete from users";
        jdbcTemplate.update(sqlQuery4);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        assertTrue("Обнаружены неучтенные данные о фильмах", filmService.findAll().isEmpty());
    }

    @Test
    void findAllGenre() {
        assertEquals(6, genreController.findAll().size(),
                "Обнаружены неучтенные данные о жанрах");
    }

    @Test
    void findAllRatings() {
        assertEquals(5, ratingController.findAll().size(),
                "Обнаружены неучтенные данные о рейтингах");
    }

    @Test
    void getGenreById() {
        Genre genre = new Genre(1, "Комедия");
        assertEquals(genre, genreController.getByKey(1), "Жанры не совпадают");
    }

    @Test
    void getRatingById() {
        Rating rating = new Rating(1, "G");
        assertEquals(rating, ratingController.getByKey(1), "Рейтинги не совпадают");
    }

    @Test
    void shouldThrowMethodNotAllowedExceptionWhenCreateNewRating() {
        MethodNotAllowedException ex1 = Assertions.assertThrows(
                MethodNotAllowedException.class,
                () -> ratingController.create(new Rating(null, "rating"))
        );
        Assertions.assertEquals("405 METHOD_NOT_ALLOWED \"Request method 'POST' not supported\"", ex1.getMessage());
    }

    @Test
    void shouldThrowMethodNotAllowedExceptionWhenPutNewRating() {
        MethodNotAllowedException ex1 = Assertions.assertThrows(
                MethodNotAllowedException.class,
                () -> ratingController.put(new Rating(null, "rating"))
        );
        Assertions.assertEquals("405 METHOD_NOT_ALLOWED \"Request method 'PUT' not supported\"", ex1.getMessage());
    }

    @Test
    void shouldThrowMethodNotAllowedExceptionWhenCreateNewGenre() {
        MethodNotAllowedException ex1 = Assertions.assertThrows(
                MethodNotAllowedException.class,
                () -> genreController.create(new Genre(null, "genre"))
        );
        Assertions.assertEquals("405 METHOD_NOT_ALLOWED \"Request method 'POST' not supported\"", ex1.getMessage());
    }

    @Test
    void shouldThrowMethodNotAllowedExceptionWhenPutNewGenre() {
        MethodNotAllowedException ex1 = Assertions.assertThrows(
                MethodNotAllowedException.class,
                () -> genreController.put(new Genre(null, "genre"))
        );
        Assertions.assertEquals("405 METHOD_NOT_ALLOWED \"Request method 'PUT' not supported\"", ex1.getMessage());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewObject() {
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.put(film)
        );
        Assertions.assertEquals("Film with Id: null not found", ex1.getMessage());
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionWhenObjectDataAlreadyExist() {
        filmService.create(film);
        film1 = new Film(null, "testFilm1Name", "d1",
                LocalDate.of(2020, 1, 1), 8500, mpa, genreIdSet, 0);
        film1.setId(film.getId());
        ObjectAlreadyExistException ex1 = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> filmService.create(film1)
        );
        Assertions.assertEquals("Film Id: " + film.getId()
                + " should be null, Id генерируется автоматически.", ex1.getMessage());
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        filmService.create(film);
        int id = 999;
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getByKey(id)
        );
        assertEquals("Film with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        filmService.create(film);
        int id = film.getId();
        assertEquals(film, filmService.getByKey(id), "Фильмы не совпадают");
    }

    @Test
    void create() {
        Rating rating = new Rating(1, null);
        Film test = new Film(null, "nisi eiusmod", "adipisicing",
                LocalDate.of(1967, 3, 25), 100, rating, null, null);
        filmService.create(test);
    }

    @Test
    void addLikeShouldThrowNoSuchElementExceptionWhenFilmIdIncorrect() {

        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getByKey(filmId).getRate(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.addLike(nonExistId, userId)
        );
        assertEquals("Film with Id: " + nonExistId + " not found", ex.getMessage());
    }

    @Test
    void deleteLikeShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        film = new Film(null, "testFilm1Name", "d1",
                LocalDate.of(2020, 1, 1), 1500, mpa, genreIdSet, 0);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getByKey(filmId).getRate(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.deleteLike(filmId, nonExistId)
        );
        assertEquals("User with Id: " + nonExistId + " not found", ex.getMessage());
    }

    @Test
    void deleteLikeShouldThrowNoSuchElementExceptionWhenFilmIdIncorrect() {

        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getByKey(filmId).getRate(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.deleteLike(nonExistId, userId)
        );
        assertEquals("Film with Id: " + nonExistId + " not found", ex.getMessage());
    }

    @Test
    void deleteLike() {

        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getByKey(filmId).getRate(),
                "Количество лайков не совпадает");
        filmService.deleteLike(filmId, userId);
        assertTrue("Лайк не был удален", filmService.getByKey(filmId).getRate() == 0);
    }

    @Test
    void getPopularFilms() {
        film1 = new Film(null, "testFilm1Name", "d1",
                LocalDate.of(2020, 1, 1), 8500, mpa, genreIdSet, 0);
        User user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        filmService.create(film);
        filmService.create(film1);
        int filmId = film.getId();
        int film1Id = film1.getId();
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        filmService.addLike(filmId, userId);
        filmService.addLike(filmId, user1Id);

        assertEquals(2, filmService.getByKey(filmId).getRate(),
                "Количество лайков не совпадает");
        filmService.addLike(film1Id, user1Id);
        assertEquals(1, filmService.getByKey(film1Id).getRate(),
                "Количество лайков не совпадает");
        assertTrue("Фильмы не совпадают",
                filmService.getPopularFilms(1).contains(filmService.getByKey(filmId)));
        assertEquals(2, filmService.getPopularFilms(2).size(),
                "Количество фильмов не совпадает");
    }
}
