package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class ObjectValidationTests {
    Film film;
    Film film1;
    User user;
    User user1;
    UserRepository userRepository;
    FilmRepository filmRepository;
    UserService userService;
    FilmService filmService;
    UserController userController;
    FilmController filmController;

    @BeforeEach
    void createSomeData() {
        userRepository = new UserRepository();
        filmRepository = new FilmRepository();
        userService = new UserService(userRepository);
        filmService = new FilmService(filmRepository);
        userController = new UserController(userService);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewObject() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userController.put(user)
        );
        Assertions.assertEquals("User doesn't exist", ex.getMessage());
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
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        userController.create(user);
        user1 = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        user1.setId(user.getId());
        ObjectAlreadyExistException ex = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> userController.create(user1)
        );
        Assertions.assertEquals("Пользователь с электронной почтой" +
                " testuser@gmail.com уже зарегистрирован.", ex.getMessage());

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
    void shouldThrowValidationExceptionWhenUserBirthdayInFuture() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 4, 1));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        Assertions.assertEquals("User validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserBirthdayIsNull() {
        user1 = new User(null, "testuser@gmail.com", "testUser",
                " ", null);
        ValidationException ex1 = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user1)
        );
        Assertions.assertEquals("User validation fail", ex1.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsBlank() {
        user = new User(null, " ", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        Assertions.assertEquals("User validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsNull() {
        user1 = new User(null, null, "testUser",
                " ", LocalDate.of(2023, 1, 1));
        ValidationException ex1 = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user1)
        );
        Assertions.assertEquals("User validation fail", ex1.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserEmailIsNotValid() {
        user = new User(null, "это-неправильный?эмейл@", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        Assertions.assertEquals("User validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginIsBlank() {
        user = new User(null, "testuser@gmail.com", " ",
                " ", LocalDate.of(2023, 1, 1));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        Assertions.assertEquals("User validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenUserLoginIsNull() {
        user1 = new User(null, "testuser@gmail.com", null,
                " ", LocalDate.of(2023, 1, 1));
        ValidationException ex1 = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user1)
        );
        Assertions.assertEquals("User validation fail", ex1.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmNameIsBlank() {
        film = new Film(null, " ", "d",
                LocalDate.of(2020, 1, 1), 8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenFilmNameIsNull() {
        film1 = new Film(null, null, "d",
                LocalDate.of(2020, 1, 1), 8500);
        ValidationException ex1 = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film1)
        );
        Assertions.assertEquals("Film validation fail", ex1.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionSizeIsMoreThan200() {
        char[] charArray = new char[201];
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionSizeIsLessThan1() {
        film = new Film(null, "testFilmName", "",
                LocalDate.of(2020, 1, 1), 8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsBefore28121895() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(1895, 12, 27), 8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenReleaseDateIsNull() {
        film1 = new Film(null, "testFilmName", "d",
                null, 8500);
        ValidationException ex1 = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film1)
        );
        Assertions.assertEquals("Film validation fail", ex1.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationIsNegative() {
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), -8500);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("Film validation fail", ex.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDurationIsNull() {
        film1 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), null);
        IllegalArgumentException ex1 = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> filmController.create(film)
        );
        Assertions.assertEquals("HV000116: The object to be validated must not be null.", ex1.getMessage());
    }

    @Test
    void create() {
        char[] charArray = new char[200];
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        userController.create(user);
        filmController.create(film);
        assertEquals("Количество пользователей не совпадает", 1, userController.findAll().size());
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
    }

    @Test
    void put() {
        char[] charArray = new char[200];
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        film = new Film(null, "testFilmName", String.valueOf(charArray),
                LocalDate.of(2020, 1, 1), 8500);
        userController.create(user);
        filmController.create(film);
        assertEquals("Количество пользователей не совпадает", 1, userController.findAll().size());
        assertEquals("Количество фильмов не совпадает", 1, filmController.findAll().size());
        user1 = new User(null, "testuser@gmail.com", "testRenewUser",
                null, LocalDate.of(2023, 1, 1));
        user1.setId(user.getId());
        film1 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        film1.setId(film.getId());
        userController.put(user1);
        filmController.put(film1);
        User user2 = new User(null, "testuser@gmail.com", "testRenewUser",
                null, LocalDate.of(2023, 1, 1));
        Film film2 = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        user2.setId(user.getId());
        film2.setId(film.getId());
        assertEquals("Пользователи не совпадают", user2, user1);
        assertEquals("Фильмы не совпадают", film2, film1);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        film = new Film(null, "testFilmName", "d",
                LocalDate.of(2020, 1, 1), 8500);
        assertTrue("Обнаружены неучтенные данные о пользователях", userController.findAll().isEmpty());
        assertTrue("Обнаружены неучтенные данные о фильмах", filmController.findAll().isEmpty());
    }
}
