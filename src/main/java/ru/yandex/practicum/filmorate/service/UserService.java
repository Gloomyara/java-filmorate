package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ObjectService<User> {
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    @Override
    public boolean repositoryContains(Integer id) {
        return userRepository.getById(id) != null;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> collection = userRepository.findAll();
        log.debug(
                "Запрос списка пользователей успешно выполнен, всего пользователей: {}",
                collection.size()
        );
        return collection;
    }

    @Override
    public User getById(Integer userId) throws ObjectNotFoundException {
        try {
            User user = Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with Id: " + userId + " not found")
            );
            log.debug(
                    "Запрос пользователя по Id: {} успешно выполнен.", userId
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public User create(User user) throws ObjectAlreadyExistException {

        if (repositoryContains(user.getId())) {
            log.warn(
                    "Пользователь с электронной почтой {} уже зарегистрирован.",
                    user.getEmail()
            );
            throw new ObjectAlreadyExistException(
                    "Пользователь с электронной почтой " +
                            user.getEmail() + " уже зарегистрирован."
            );
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(id);
        log.debug(
                "Пользователь с электронной почтой {} успешно зарегистрирован.",
                user.getEmail()
        );
        userRepository.put(id, user);
        id++;
        return user;
    }

    @Override
    public User put(User user) throws ObjectNotFoundException {
        try {
            Integer userId = user.getId();
            Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + userId + " doesn't exist!")
            );
            userRepository.put(userId, user);
            log.debug(
                    "Данные пользователя с электронной почтой {} успешно обновлены.",
                    user.getEmail()
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public User addFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {

        try {
            User user = Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + userId + " doesn't exist!")
            );
            User friend = Optional.ofNullable(userRepository.getById(friendId)).orElseThrow(
                    () -> new ObjectNotFoundException("Error! Cannot add friend with id:" + friendId
                            + ", user doesn't exist!")
            );
            user.addFriend(friendId);
            friend.addFriend(userId);
            log.debug(
                    "Запрос пользователя под Id: {} на добавление в друзья, " +
                            "пользователя Id: {}, успешно выполнен!\n" +
                            "Всего друзей в списке: {}.", userId, friendId, user.getFriends().size()
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public User deleteFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {

        try {
            User user = Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + userId + " doesn't exist!")
            );
            User friend = Optional.ofNullable(userRepository.getById(friendId)).orElseThrow(
                    () -> new ObjectNotFoundException("Error! Cannot delete friend with id: "
                            + friendId + ", user doesn't in your friends list!")
            );
            Optional.ofNullable(friendId)
                    .filter((p) -> user.getFriends().contains(friendId))
                    .orElseThrow(
                            () -> new ObjectNotFoundException("Error! Cannot delete friend with id: "
                                    + friendId + ", user doesn't in your friends list!")
                    );
            user.deleteFriend(friendId);
            friend.deleteFriend(userId);
            log.debug(
                    "Запрос пользователя под Id: {} на удаление из друзей, " +
                            "пользователя Id: {}, успешно выполнен!\n" +
                            "Всего друзей в списке: {}.", userId, friendId, user.getFriends().size()
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Collection<User> getFriendsListById(Integer userId) throws ObjectNotFoundException {
        try {
            User user = Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + userId + " doesn't exist!")
            );
            log.debug(
                    "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                            "Всего друзей в списке: {}.", userId, user.getFriends().size()
            );
            return user.getFriends().stream()
                    .map(this::getById)
                    .collect(Collectors.toList());

        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Collection<User> getMutualFriendsList(
            Integer userId, Integer otherId) throws ObjectNotFoundException {

        try {
            User user = Optional.ofNullable(userRepository.getById(userId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + userId + " doesn't exist!")
            );
            User otherUser = Optional.ofNullable(userRepository.getById(otherId)).orElseThrow(
                    () -> new ObjectNotFoundException("User with id: " + otherId + " doesn't exist!")
            );
            Set<Integer> mutualFriendsSet = new HashSet<>(user.getFriends());
            mutualFriendsSet.retainAll(otherUser.getFriends());
            log.debug(
                    "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                            "Всего общих друзей: {}.", userId, otherId, mutualFriendsSet.size()
            );
            return mutualFriendsSet.stream()
                    .map(this::getById)
                    .collect(Collectors.toList());

        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
