package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.*;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class FilmValidationTests {
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Film>> violations;
    Film film;
    Film film1;
    FilmRepository filmRepository;
    FilmService filmService;
    FilmController filmController;

    @BeforeEach
    void createSomeData() {
        filmRepository = new FilmRepository();
        filmService = new FilmService(filmRepository);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewObject() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> filmController.put(film)
        );
        Assertions.assertEquals("Film doesn't exist", ex1.getMessage());
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionWhenObjectDataAlreadyExist() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        filmController.create(film);
        film1 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        film1.setId(film.getId());
        ObjectAlreadyExistException ex1 = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> filmController.create(film1)
        );
        Assertions.assertEquals("Фильм под названием testFilmName уже есть в списке фильмов.", ex1.getMessage());
    }

    @Test
    void shouldNotPassValidationWhenFilmNameIsBlank() {
        film = new Film(null, " ", "d",
                LocalDate.of(2020, 1, 1), 8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Name cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenFilmNameIsNull() {
        film = new Film(null, null, "d",
                LocalDate.of(2020, 1, 1), 8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Name cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenDescriptionSizeIsMoreThan200() {
        char[] charArray = new char[201];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Description must be between 1 and 200 characters",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenDescriptionSizeIsLessThan1() {
        film = new Film(null, "testFilmName", "",
                LocalDate.of(2020, 1, 1), 8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Description must be between 1 and 200 characters",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsBefore28121895() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(1895, 12, 27), 8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film ReleaseDate isBefore 28-12-1895", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenReleaseDateIsNull() {
        film1 = new Film(null, "testFilmName", "d",
                null, 8500);
        NullPointerException ex1 = Assertions.assertThrows(
                NullPointerException.class,
                () -> filmController.create(film1)
        );
        Assertions.assertNull(ex1.getMessage());
    }

    @Test
    void shouldNotPassValidationWhenDurationIsNegative() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), -8500);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Duration should be positive",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenDurationIsNull() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), null);
        violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(
                "Duration cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void create() {
        char[] charArray = new char[200];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        filmController.create(film);
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
    }

    @Test
    void put() {
        char[] charArray = new char[200];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        filmController.create(film);
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
        film1 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        film1.setId(film.getId());
        filmController.put(film1);
        Film film2 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        film2.setId(film.getId());
        assertEquals("Фильмы не совпадают", film2, film1);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        assertTrue("Обнаружены неучтенные данные о фильмах", filmController.findAll().isEmpty());
    }
}