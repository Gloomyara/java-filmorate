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
        return userRepository.contains(id);
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
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with Id: " + userId + " not found")
                    );
            log.debug(
                    "Запрос пользователя по Id: {} успешно выполнен.", i
            );
            return userRepository.getById(i);
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
        Optional<Integer> optionalUserId = Optional.ofNullable(user.getId());
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + user.getId() + " doesn't exist!")
                    );
            userRepository.put(i, user);
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
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        Optional<Integer> optionalFriendId = Optional.ofNullable(friendId);
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + userId + " doesn't exist!")
                    );
            int j = optionalFriendId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "Error! Cannot add friend with id:" + friendId
                                            + ", user doesn't exist!")
                    );

            User initialUser = userRepository.getById(i);
            initialUser.addFriend(j);
            userRepository.getById(j).addFriend(i);
            log.debug(
                    "Запрос пользователя под Id: {} на добавление в друзья, " +
                            "пользователя Id: {}, успешно выполнен!\n" +
                            "Всего друзей в списке: {}.", i, j, initialUser.getFriends().size()
            );
            return initialUser;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public User deleteFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        Optional<Integer> optionalFriendId = Optional.ofNullable(friendId);
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + userId + " doesn't exist!")
                    );
            User initialUser = userRepository.getById(i);

            int j = optionalFriendId
                    .filter(this::repositoryContains)
                    .filter((p) -> initialUser.getFriends().contains(optionalFriendId.orElse(null)))
                    .orElseThrow(
                            () -> new ObjectNotFoundException("Error! Cannot delete friend with id: "
                                    + friendId + ", user doesn't in your friends list!")
                    );

            initialUser.deleteFriend(j);
            userRepository.getById(j).deleteFriend(i);
            log.debug(
                    "Запрос пользователя под Id: {} на удаление из друзей, " +
                            "пользователя Id: {}, успешно выполнен!\n" +
                            "Всего друзей в списке: {}.", i, j, initialUser.getFriends().size()
            );
            return initialUser;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Collection<User> getFriendsListById(Integer userId) throws ObjectNotFoundException {
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + userId + " doesn't exist!")
                    );
            User user = userRepository.getById(i);
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

        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        Optional<Integer> optionalFriendId = Optional.ofNullable(otherId);
        try {
            int i = optionalUserId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + userId + " doesn't exist!")
                    );
            int j = optionalFriendId
                    .filter(this::repositoryContains)
                    .orElseThrow(
                            () -> new ObjectNotFoundException(
                                    "User with id: " + otherId + " doesn't exist!")
                    );

            User user = userRepository.getById(i);
            Set<Integer> mutualFriendsSet = new HashSet<>(user.getFriends());
            User otherUser = userRepository.getById(j);
            mutualFriendsSet.retainAll(otherUser.getFriends());
            log.debug(
                    "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                            "Всего общих друзей: {}.", i, j, mutualFriendsSet.size()
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

