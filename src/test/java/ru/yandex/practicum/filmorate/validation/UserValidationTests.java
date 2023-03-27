package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class UserValidationTests {
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<User>> violations;
    User user;
    User user1;
    InMemoryUserRepository inMemoryUserRepository;
    UserService userService;
    UserController userController;

    @BeforeEach
    void createSomeData() {
        inMemoryUserRepository = new InMemoryUserRepository();
        userService = new UserService(inMemoryUserRepository);
        userController = new UserController(userService);
    }

    @AfterEach
    void clearUserRepository() {
        inMemoryUserRepository.getStorage().clear();
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewUser() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userController.put(user)
        );
        assertEquals("User with id: null doesn't exist!", ex.getMessage());
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionWhenUserDataAlreadyExist() {
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
        assertEquals("Пользователь с электронной почтой" +
                " testuser@gmail.com уже зарегистрирован.", ex.getMessage());
    }

    @Test
    void shouldNotPassValidationWhenUserBirthdayInFuture() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 4, 1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Birthday should be in the past",
                violations.iterator().next().getMessage()
        );
    }

    void shouldNotPassValidationWhenUserBirthdayIsNull() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", null);
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Birthday cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsBlank() {
        user = new User(null, " ", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email should be valid",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsNull() {
        user = new User(null, null, "testUser",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsNotValid() {
        user = new User(null, "это-неправильный?эмейл@", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email should be valid",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserLoginIsBlank() {
        user = new User(null, "testuser@gmail.com", " ",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Login cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserLoginIsNull() {
        user1 = new User(null, "testuser@gmail.com", null,
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user1);
        assertEquals(1, violations.size());
        assertEquals(
                "Login cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void create() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        if (violations.isEmpty()) {
            userController.create(user);
        }
        AssertionErrors.assertEquals("Количество пользователей не совпадает",
                1, userController.findAll().size());
    }

    @Test
    void put() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        violations = validator.validate(user);
        if (violations.isEmpty()) {
            userController.create(user);
        }
        AssertionErrors.assertEquals("Количество пользователей не совпадает",
                1, userController.findAll().size());
        user1 = new User(null, "testuser@gmail.com", "testRenewUser",
                null, LocalDate.of(2023, 1, 1));
        user1.setId(user.getId());
        violations = validator.validate(user1);
        if (violations.isEmpty()) {
            userController.put(user1);
        }
        User user2 = new User(null, "testuser@gmail.com", "testRenewUser",
                null, LocalDate.of(2023, 1, 1));
        user2.setId(user.getId());
        AssertionErrors.assertEquals("Пользователи не совпадают", user2, user1);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        assertTrue("Обнаружены неучтенные данные о пользователях", userController.findAll().isEmpty());
    }
}
