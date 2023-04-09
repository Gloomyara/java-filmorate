package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.film.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryGenreRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryRatingRepository;
import ru.yandex.practicum.filmorate.repository.user.inmemory.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class FilmValidationTests {
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Film>> violations;
    Film film;
    Film film1;
    InMemoryFilmRepository filmRepository;

    FilmService filmService;
    FilmController filmController;
    Set<Genre> genreIdSet;

    @BeforeEach
    void createSomeData() {
        Genre genre = new Genre(null, "genre1");
        Rating rating = new Rating(null, "rating1");
        char[] charArray = new char[200];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500, null, genreIdSet, 0);
        film1 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500, null, genreIdSet, 0);
        var genreRepository = new InMemoryGenreRepository();
        var ratingRepository = new InMemoryRatingRepository();
        var userRepository = new InMemoryUserRepository();
        genreRepository.create(genre);
        ratingRepository.create(rating);
        genreIdSet = new HashSet<>();
        genreIdSet.add(genre);
        filmRepository = new InMemoryFilmRepository();
        filmService = new FilmService(filmRepository, genreRepository, ratingRepository, userRepository);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewObject() {
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmController.put(film)
        );
        Assertions.assertEquals("Film with Id: null not found", ex1.getMessage());
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionWhenObjectDataAlreadyExist() {
        filmController.create(film);
        film1.setId(film.getId());
        ObjectAlreadyExistException ex1 = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> filmController.create(film1)
        );
        Assertions.assertEquals("Film Id: " + film.getId()
                + " should be null, Id генерируется автоматически.", ex1.getMessage());
    }

    @Test
    void shouldNotPassValidationWhenFilmTitleIsBlank() {
        film.setName(" ");
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Title cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenFilmTitleIsNull() {
        film.setName(null);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Title cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenDescriptionSizeIsMoreThan200() {
        char[] charArray = new char[201];
        film.setDescription(String.valueOf(charArray));
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Description must be between 1 and 200 characters",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenDescriptionSizeIsLessThan1() {
        film.setDescription("");
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Description must be between 1 and 200 characters",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenReleaseDateIsBefore28121895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "ReleaseDate invalid",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenLengthIsNegative() {
        film.setDuration(-8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Length should be positive",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenLengthIsNull() {
        film.setDuration(null);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Length cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void createShouldPassValidation() {
        violations = validator.validate(film);
        if (violations.isEmpty()) {
            filmController.create(film);
        }
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
    }

    @Test
    void putShouldPassValidation() {
        violations = validator.validate(film);
        if (violations.isEmpty()) {
            filmController.create(film);
        }
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
        film1.setId(film.getId());
        violations = validator.validate(film1);
        if (violations.isEmpty()) {
            filmController.put(film1);
        }
        Film film2 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500, null, null, 0);
        film2.setId(film.getId());
        assertEquals("Фильмы не совпадают", film2, film1);
    }
}
