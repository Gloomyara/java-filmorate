package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class UserServiceTest {

    User user;
    User user1;
    InMemoryUserRepository inMemoryUserRepository;
    UserService userService;

    @BeforeEach
    void createSomeData() {
        inMemoryUserRepository = new InMemoryUserRepository();
        userService = new UserService(inMemoryUserRepository);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        assertTrue("Обнаружены не учтенные данные о пользователях", userService.findAll().isEmpty());
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 1;
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getById(id)
        );
        assertEquals("User with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        userService.create(user);
        assertEquals(1, userService.findAll().size(), "Фильм не был добавлен в репозиторий");
        int id = user.getId();
        user1 = new User(id, "testuser@gmail.com", "testUser",
                "testUser", LocalDate.of(2023, 1, 1));
        assertEquals(user1, userService.getById(id), "Фильмы не совпадают");
    }

    @Test
    void addFriendShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        int nonExistId1 = 9999;
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.addFriend(userId, nonExistId)
        );
        assertEquals("Error! Cannot add friend with id:" + nonExistId
                + ", user doesn't exist!", ex.getMessage());
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.addFriend(nonExistId1, user1Id)
        );
        assertEquals("User with id: " + nonExistId1 + " doesn't exist!", ex1.getMessage());
    }

    @Test
    void addFriend() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
    }

    @Test
    void deleteFriendShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        int nonExistId1 = 9999;
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteFriend(userId, nonExistId)
        );
        assertEquals("Error! Cannot delete friend with id: " + nonExistId
                + ", user doesn't in your friends list!", ex.getMessage());
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteFriend(nonExistId1, user1Id)
        );
        assertEquals("User with id: " + nonExistId1 + " doesn't exist!", ex1.getMessage());
    }

    @Test
    void deleteFriend() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
        userService.deleteFriend(userId, user1Id);
        assertTrue("Обнаружены не учтенные данные о пользователях", userService.getFriendsListById(userId).isEmpty());
        assertTrue("Обнаружены не учтенные данные о пользователях", userService.getFriendsListById(user1Id).isEmpty());
    }

    @Test
    void getFriendsListByIdShouldThrowNoSuchElementException() {
        int id = 1;
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getFriendsListById(id)
        );
        assertEquals("User with id: " + id + " doesn't exist!", ex.getMessage());
    }

    @Test
    void getFriendsListByIdShouldBeIsEmpty() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        userService.create(user);
        int userId = user.getId();
        assertTrue("Обнаружены не учтенные данные о пользователях",
                userService.getFriendsListById(userId).isEmpty());
    }

    @Test
    void getFriendsListById() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
        assertTrue("Пользователи не совпадают",
                userService.getFriendsListById(userId).contains(user1));
    }

    @Test
    void getMutualFriendsListShouldBeIsEmpty() {
        Function<User, Integer> getId = (User::getId);
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        User user2 = new User(null, "testuser2@gmail.com", "testUser2",
                " ", LocalDate.of(2003, 1, 1));
        User user3 = new User(null, "testuser2@gmail.com", "testUser2",
                " ", LocalDate.of(1993, 1, 1));
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.addFriend(getId.apply(user), getId.apply(user1));
        userService.addFriend(getId.apply(user), getId.apply(user2));
        userService.addFriend(getId.apply(user1), getId.apply(user2));
        assertTrue("Обнаружены не учтенные данные о друзьях",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user3)).isEmpty());
    }

    @Test
    void getMutualFriendsListShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        int nonExistId1 = 9999;
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getMutualFriendsList(userId, nonExistId)
        );
        assertEquals("User with id: " + nonExistId + " doesn't exist!", ex.getMessage());
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getMutualFriendsList(nonExistId1, user1Id)
        );
        assertEquals("User with id: " + nonExistId1 + " doesn't exist!", ex1.getMessage());
    }

    @Test
    void getMutualFriendsList() {
        Function<User, Integer> getId = (User::getId);
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
        User user2 = new User(null, "testuser2@gmail.com", "testUser2",
                " ", LocalDate.of(2003, 1, 1));
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.addFriend(getId.apply(user), getId.apply(user1));
        userService.addFriend(getId.apply(user), getId.apply(user2));
        userService.addFriend(getId.apply(user1), getId.apply(user2));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user1)).contains(user2));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user2)).contains(user1));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user2), getId.apply(user1)).contains(user));
    }
}