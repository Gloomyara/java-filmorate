package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.inmemory.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTests {
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<User>> violations;
    User user;
    User user1;
    InMemoryUserRepository userRepository;
    UserService userService;
    UserController userController;

    @BeforeEach
    void createSomeData() {
        user = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser@gmail.com", "testRenewUser",
                null, LocalDate.of(2023, 1, 1));
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
        userController = new UserController(userService);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenPutNewUser() {
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userController.put(user)
        );
        assertEquals("User with Id: null not found", ex.getMessage());
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionWhenUserDataAlreadyExist() {
        userController.create(user);
        User user2 = new User(null, "testuser@gmail.com", "testUser",
                " ", LocalDate.of(2023, 1, 1));
        user2.setId(user.getId());
        ObjectAlreadyExistException ex = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> userController.create(user2)
        );
        assertEquals("User Id: " + user.getId()
                + " should be null, Id генерируется автоматически.", ex.getMessage());
    }

    @Test
    void shouldNotPassValidationWhenUserBirthdayInTheFuture() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Birthday should be in the past",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserBirthdayIsNull() {
        user.setBirthday(null);
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Birthday cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsBlank() {
        user.setEmail(" ");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email should be valid",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsNull() {
        user.setEmail(null);
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email cannot be null",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserEmailIsNotValid() {
        user.setEmail("это-неправильный?эмейл@");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Email should be valid",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserLoginIsBlank() {
        user.setLogin(" ");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Login cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldNotPassValidationWhenUserLoginIsNull() {
        user.setLogin(null);
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(
                "Login cannot be blank",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void create() {
        violations = validator.validate(user);
        if (violations.isEmpty()) {
            userController.create(user);
        }
        AssertionErrors.assertEquals("Количество пользователей не совпадает",
                1, userController.findAll().size());
    }

    @Test
    void put() {
        violations = validator.validate(user);
        if (violations.isEmpty()) {
            userController.create(user);
        }
        AssertionErrors.assertEquals("Количество пользователей не совпадает",
                1, userController.findAll().size());
        user1 = new User(null, "testuser@gmail.com", "testRenewUser",
                user.getName(), LocalDate.of(2023, 1, 1));
        user1.setId(user.getId());
        violations = validator.validate(user1);
        if (violations.isEmpty()) {
            userController.put(user1);
        }
        User user2 = new User(null, "testuser@gmail.com", "testRenewUser",
                user.getName(), LocalDate.of(2023, 1, 1));
        user2.setId(user.getId());
        AssertionErrors.assertEquals("Пользователи не совпадают", user2, this.user1);
    }
}
