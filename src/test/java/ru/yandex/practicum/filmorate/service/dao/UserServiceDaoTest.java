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
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceDaoTest {
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;
    User user;
    User user1;

    @BeforeEach
    void createSomeData() {
        user = new User(null, "testuser@gmail.com", "testUser",
                null, LocalDate.of(2023, 1, 1));
        user1 = new User(null, "testuser1@gmail.com", "testUser1",
                " ", LocalDate.of(2013, 1, 1));
    }

    @AfterEach
    void deleteSomeData() {

        String sqlQuery1 = "delete from friends";
        jdbcTemplate.update(sqlQuery1);
        String sqlQuery2 = "delete from users";
        jdbcTemplate.update(sqlQuery2);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        assertTrue("Обнаружены не учтенные данные о пользователях", userService.findAll().isEmpty());
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 999;
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.getByKey(id)
        );
        assertEquals("User with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        userService.create(user);
        int id = user.getId();
        User user2 = new User(id, "testuser@gmail.com", "testUser",
                "testUser", LocalDate.of(2023, 1, 1));
        assertEquals(user2, userService.getByKey(id), "Пользователи не совпадают");
    }

    @Test
    void addFriendShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        int nonExistId1 = 9999;
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
        assertEquals("User with Id: " + nonExistId
                + " not found", ex.getMessage());
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.addFriend(nonExistId1, user1Id)
        );
        assertEquals("User with Id: " + nonExistId1 + " not found", ex1.getMessage());
    }

    @Test
    void addFriend() {
        userService.create(user);
        userService.create(user1);
        int userId = user.getId();
        int user1Id = user1.getId();
        userService.addFriend(userId, user1Id);
        assertEquals(1, userService.getFriendsListById(userId).size(),
                "Количество друзей не совпадает");
    }

    @Test
    void addFriendListShouldThrowObjectAlreadyExistException() {
        int id = 999;
        ObjectAlreadyExistException ex = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> userService.addFriend(id, id)
        );
        assertEquals("Ошибка! Нельзя добавить в друзья самого себя!", ex.getMessage());
    }

    @Test
    void deleteFriendShouldThrowNoSuchElementExceptionWhenUserIdIncorrect() {
        int nonExistId = 999;
        int nonExistId1 = 9999;
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
        assertEquals("Error! Cannot delete User Id: " + userId
                        + " friend with id: " + nonExistId +
                        ", user doesn't in your friends list!",
                ex.getMessage());
        NoSuchElementException ex1 = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteFriend(nonExistId1, user1Id)
        );
        assertEquals("User with Id: " + nonExistId1 + " not found", ex1.getMessage());
    }

    @Test
    void deleteFriend() {
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
    void getFriendsListByIdShouldBeIsEmpty() {
        int nonExistId = 999;
        userService.create(user);
        int userId = user.getId();
        assertTrue("Обнаружены не учтенные данные о пользователях",
                userService.getFriendsListById(userId).isEmpty());
        assertTrue("Обнаружены не учтенные данные о пользователях",
                userService.getFriendsListById(nonExistId).isEmpty());
    }

    @Test
    void getFriendsListById() {
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
        int nonExistId = 999;
        Function<User, Integer> getId = (User::getId);
        User user2 = new User(null, "testuser2@gmail.com", "testUser2",
                " ", LocalDate.of(2003, 1, 1));
        User user3 = new User(null, "testuser3@gmail.com", "testUser3",
                " ", LocalDate.of(1993, 1, 1));
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.addFriend(getId.apply(user), getId.apply(user1));
        userService.addFriend(getId.apply(user1), getId.apply(user));
        userService.addFriend(getId.apply(user), getId.apply(user2));
        userService.addFriend(getId.apply(user2), getId.apply(user));
        userService.addFriend(getId.apply(user1), getId.apply(user2));
        userService.addFriend(getId.apply(user2), getId.apply(user1));
        assertTrue("Обнаружены не учтенные данные о друзьях",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user3)).isEmpty());
        assertTrue("Обнаружены не учтенные данные о друзьях",
                userService.getMutualFriendsList(getId.apply(user), nonExistId).isEmpty());
        assertTrue("Обнаружены не учтенные данные о друзьях",
                userService.getMutualFriendsList(nonExistId, getId.apply(user)).isEmpty());
    }

    @Test
    void getMutualFriendsListShouldThrowObjectAlreadyExistException() {
        int id = 999;
        ObjectAlreadyExistException ex = Assertions.assertThrows(
                ObjectAlreadyExistException.class,
                () -> userService.getMutualFriendsList(id, id)
        );
        assertEquals("Ошибка! Нельзя запросить общий список друзей с самим собой!", ex.getMessage());
    }

    @Test
    void getMutualFriendsList() {
        Function<User, Integer> getId = (User::getId);
        User user2 = new User(null, "testuser2@gmail.com", "testUser2",
                " ", LocalDate.of(2003, 1, 1));
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.addFriend(getId.apply(user), getId.apply(user1));
        userService.addFriend(getId.apply(user1), getId.apply(user));
        userService.addFriend(getId.apply(user), getId.apply(user2));
        userService.addFriend(getId.apply(user2), getId.apply(user));
        userService.addFriend(getId.apply(user1), getId.apply(user2));
        userService.addFriend(getId.apply(user2), getId.apply(user1));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user1)).contains(user2));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user), getId.apply(user2)).contains(user1));
        assertTrue("Друзья не совпадают",
                userService.getMutualFriendsList(getId.apply(user2), getId.apply(user1)).contains(user));
    }
}
