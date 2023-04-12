package ru.yandex.practicum.filmorate.service.inmemory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryGenreRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryRatingRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.inmemory.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class FilmServiceInMemoryTest {
    Film film;
    Film film1;
    User user;
    User user1;
    UserRepository<Integer> userRepository;
    FilmRepository<Integer> filmRepository;
    FilmService filmService;
    List<Genre> genreIdSet;

    @BeforeEach
    void createSomeData() {
        Genre genre = new Genre(null, "genre1");
        Rating rating = new Rating(null, "rating1");
        var genreRepository = new InMemoryGenreRepository();
        var ratingRepository = new InMemoryRatingRepository();
        genreRepository.create(genre);
        ratingRepository.create(rating);
        userRepository = new InMemoryUserRepository();
        filmRepository = new InMemoryFilmRepository(
                (InMemoryUserRepository) userRepository,
                genreRepository, ratingRepository);
        filmService = new FilmService(filmRepository);
        genreIdSet = new ArrayList<>();
        genreIdSet.add(genre);
        char[] charArray = new char[200];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500, null, genreIdSet, 0);
        film1 = new Film(null, "testFilm1Name", "d1",
                LocalDate.of(2020, 1, 1), 1500, null, genreIdSet, 0);
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
    }

    @Test
    void findAllShouldBeIsEmpty() {
        assertTrue("Обнаружены неучтенные данные о фильмах", filmService.findAll().isEmpty());
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 1;
        film.setId(id);
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.getByKey(id)
        );
        assertEquals("Film with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        filmService.create(film);
        assertEquals(1, filmService.findAll().size(), "Фильм не был добавлен в репозиторий");
        int id = film.getId();
        Film film2 = new Film(id, "testFilmName", film.getDescription(),
                LocalDate.of(2020, 1, 1), 8500, null, genreIdSet, 0);
        assertEquals(film2, filmService.getByKey(id), "Фильмы не совпадают");
    }

    @Test
    void addLikeShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.addLike(filmId, nonExistId)
        );
        assertEquals("User with Id: " + nonExistId + " not found", ex.getMessage());
    }

    @Test
    void addLikeShouldThrowNoSuchElementExceptionWhenFilmIdIncorrect() {

        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
                "Количество лайков не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmService.addLike(nonExistId, userId)
        );
        assertEquals("Film with Id: " + nonExistId + " not found", ex.getMessage());
    }


    @Test
    void addLike() {
        filmService.create(film);
        int filmId = film.getId();
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
                "Количество лайков не совпадает");
    }

    @Test
    void deleteLikeShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        filmService.create(film);
        int filmId = film.getId();
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
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
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
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
        userRepository.create(user);
        int userId = user.getId();
        filmService.addLike(filmId, userId);
        assertEquals(1, film.getRate(),
                "Количество лайков не совпадает");
        filmService.deleteLike(filmId, userId);
        assertTrue("Лайк не был удален", film.getRate() == 0);
    }

    @Test
    void getPopularFilmsShouldBeIsEmpty() {
        assertTrue("Обнаружены неучтенные данные о фильмах",
                filmService.getPopularFilms(10).isEmpty());
    }

    @Test
    void getPopularFilms() {

        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        filmService.create(film);
        filmService.create(film1);
        int filmId = film.getId();
        int film1Id = film1.getId();
        userRepository.create(user);
        userRepository.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        filmService.addLike(filmId, userId);
        filmService.addLike(filmId, user1Id);

        assertEquals(2, film.getRate(),
                "Количество лайков не совпадает");
        filmService.addLike(film1Id, user1Id);
        assertEquals(1, film1.getRate(),
                "Количество лайков не совпадает");
        assertTrue("Фильмы не совпадают",
                filmService.getPopularFilms(1).contains(filmService.getByKey(filmId)));
        assertEquals(2, filmService.getPopularFilms(2).size(),
                "Количество фильмов не совпадает");
    }
}
