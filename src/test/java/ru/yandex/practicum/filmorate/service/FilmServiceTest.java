package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class FilmServiceTest {
    Film film;
    Film film1;
    User user;
    User user1;
    InMemoryUserRepository inMemoryUserRepository;
    InMemoryFilmRepository inMemoryFilmRepository;
    FilmService filmService;
    UserService userService;

    @BeforeEach
    void createSomeData() {
        inMemoryUserRepository = new InMemoryUserRepository();
        inMemoryFilmRepository = new InMemoryFilmRepository();
        filmService = new FilmService(inMemoryFilmRepository, inMemoryUserRepository);
        userService = new UserService(inMemoryUserRepository);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        assertTrue("Обнаружены неучтенные данные о фильмах", filmService.findAll().isEmpty());
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 1;
        film = new Film(id, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getById(id)
        );
        assertEquals("Film with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        filmService.create(film);
        assertEquals(1, filmService.findAll().size(), "Фильм не был добавлен в репозиторий");
        int id = film.getId();
        film1 = new Film(id, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        assertEquals(film1, filmService.getById(id), "Фильмы не совпадают");
    }

    @Test
    void addLikeShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int userId = 9;
        int nonExistId = 999;
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(userId, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        inMemoryUserRepository.put(userId, user);
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.addLike(filmId, nonExistId)
        );
        assertEquals("User Id:" + nonExistId + " doesn't exist", ex.getMessage());
    }

    @Test
    void addLikeShouldThrowNoSuchElementExceptionWhenFilmIdIncorrect() {
        int userId = 9;
        int nonExistId = 999;
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(userId, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        inMemoryUserRepository.put(userId, user);
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.addLike(nonExistId, userId)
        );
        assertEquals("Film with Id: " + nonExistId + " not found", ex.getMessage());
    }


    @Test
    void addLike() {
        int userId = 9;
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(userId, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        inMemoryUserRepository.put(userId, user);
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
    }

    @Test
    void deleteLikeShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.deleteLike(filmId, nonExistId)
        );
        assertEquals("Error! Cannot delete user Id: " + nonExistId
                + " like, user like not found.", ex.getMessage());
    }

    @Test
    void deleteLikeShouldThrowNoSuchElementExceptionWhenFilmIdIncorrect() {
        int userId = 9;
        int nonExistId = 999;
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(userId, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        inMemoryUserRepository.put(userId, user);
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.deleteLike(nonExistId, userId)
        );
        assertEquals("Film with Id: " + nonExistId + " not found", ex.getMessage());
    }

    @Test
    void deleteLike() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        filmService.create(film);
        int filmId = film.getId();
        userService.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        filmService.deleteLike(filmId, userId);
        assertTrue("Лайк не был удален", filmService.getById(filmId).getLikesInfo().isEmpty());
    }

    @Test
    void getPopularFilmsShouldBeIsEmpty() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        assertTrue("Обнаружены неучтенные данные о фильмах",
                filmService.getPopularFilms(10).isEmpty());
    }

    @Test
    void getPopularFilms() {

        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        film1 = new Film(null, "testFilm1Name", "d1",
                LocalDate.of(2010, 1, 1), 1500);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
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
        System.out.println(filmService.getById(filmId).getLikesInfo().size());
        assertEquals(2, filmService.getById(filmId).getLikesInfo().size(),
                "Количество лайков не совпадает");
        filmService.addLike(film1Id, user1Id);
        assertEquals(1, filmService.getById(film1Id).getLikesInfo().size(),
                "Количество лайков не совпадает");
        assertTrue("Фильмы не совпадают",
                filmService.getPopularFilms(1).contains(filmService.getById(filmId)));
        assertEquals(2, filmService.getPopularFilms(2).size(),
                "Количество фильмов не совпадает");
    }
}
