package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService implements ObjectService<User> {
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    @Autowired
    public UserService(UserRepository<Integer> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean repositoryContains(Integer id) {
        return userRepository.getStorage().containsKey(id);
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
        if (repositoryContains(userId)) {
            log.debug(
                    "Запрос пользователя по Id: {} успешно выполнен.", userId
            );
            return userRepository.getById(userId);
        } else {
            log.warn(
                    "Пользователь c Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User with Id: " + userId + " not found"
            );
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
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        id++;
        log.debug(
                "Пользователь с электронной почтой {} успешно зарегистрирован.",
                user.getEmail()
        );
        userRepository.put(user.getId(), user);
        return user;
    }

    @Override
    public User put(User user) throws ObjectNotFoundException {

        if (repositoryContains(user.getId())) {
            userRepository.put(user.getId(), user);
            log.debug(
                    "Данные пользователя с электронной почтой {} успешно обновлены.",
                    user.getEmail()
            );
        } else {
            log.warn(
                    "Пользователь с электронной почтой {} не найден.",
                    user.getEmail()
            );
            throw new ObjectNotFoundException(
                    "User with id: " + user.getId() + " doesn't exist!"
            );
        }
        return user;
    }

    public User addFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {
        if (!repositoryContains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User with id: " + userId + " doesn't exist!"
            );
        }
        if (!repositoryContains(friendId)) {
            log.warn(
                    "Ошибка! Не удалось добавить пользователя в друзья. " +
                            "Пользователь под Id: {} не найден.", friendId
            );
            throw new ObjectNotFoundException(
                    "Error! Cannot add friend with id:" + friendId + ", user doesn't exist!"
            );
        }
        User initialUser = userRepository.getById(userId);
        initialUser.addFriend(friendId);
        userRepository.getById(friendId).addFriend(userId);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", userId, friendId, initialUser.getFriends().size()
        );
        return initialUser;
    }

    public User deleteFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {
        if (!repositoryContains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User with id: " + userId + " doesn't exist!"
            );
        }
        if (!repositoryContains(friendId)) {
            log.warn(
                    "Ошибка! Не удалось удалить пользователя из друзей. " +
                            "Пользователь под Id: {} не найден.", friendId
            );
            throw new ObjectNotFoundException(
                    "Error! Cannot delete friend with id: " + friendId + ", user doesn't exist!"
            );
        }
        User initialUser = userRepository.getById(userId);
        if (!initialUser.getFriends().contains(friendId)){
            log.warn(
                    "Ошибка! Не удалось удалить пользователя из друзей. " +
                            "Пользователя под Id: {} нет в списке друзей.", friendId
            );
            throw new ObjectNotFoundException(
                    "Error! Cannot delete friend with id: " + friendId + ", " +
                            "user doesn't in your friends list!"
            );
        }
        initialUser.deleteFriend(friendId);
        userRepository.getById(friendId).deleteFriend(userId);
        log.debug(
                "Запрос пользователя под Id: {} на удаление из друзей, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", userId, friendId, initialUser.getFriends().size()
        );
        return initialUser;
    }

    public Collection<User> getFriendsListById(Integer userId) throws ObjectNotFoundException {

        if (!repositoryContains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User with id: " + userId + " doesn't exist!"
            );
        }
        List<User> friendsList = new ArrayList<>();
        User user = userRepository.getById(userId);
        for (int i : user.getFriends()) {
            friendsList.add(userRepository.getById(i));
        }
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", userId, user.getFriends().size()
        );
        return friendsList;
    }

    public Collection<User> getMutualFriendsList(
            Integer userId, Integer otherId) throws ObjectNotFoundException {

        if (!repositoryContains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User with id: " + userId + " doesn't exist!"
            );
        }
        if (!repositoryContains(otherId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", otherId
            );
            throw new ObjectNotFoundException(
                    "User with id: " + otherId + " doesn't exist!"
            );
        }
        List<User> mutualFriendsList = new ArrayList<>();
        User user = userRepository.getById(userId);
        User otherUser = userRepository.getById(otherId);
        for (int i : user.getFriends()) {
            if (otherUser.getFriends().contains(i)) {
                mutualFriendsList.add(userRepository.getById(i));
            }
        }
        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", userId, otherId, user.getFriends().size()
        );
        return mutualFriendsList;
    }
}
