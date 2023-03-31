package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ObjectService<Integer, User> {
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    @Override
    public boolean repositoryContainsKey(Integer k) {
        return userRepository.getByKey(k) != null;
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
    public User getByKey(Integer id1) throws ObjectNotFoundException {
        try {
            User user = Optional.ofNullable(userRepository.getByKey(id1)).orElseThrow(
                    () -> new ObjectNotFoundException("User with Id: " + id1 + " not found")
            );
            log.debug(
                    "Запрос пользователя по Id: {} успешно выполнен.",
                    id1
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public User create(User u) throws ObjectAlreadyExistException {

        if (repositoryContainsKey(u.getId())) {
            log.warn(
                    "Пользователь с электронной почтой {} уже зарегистрирован.",
                    u.getEmail()
            );
            throw new ObjectAlreadyExistException("Пользователь с электронной почтой " +
                    u.getEmail() + " уже зарегистрирован.");
        }
        if (u.getName() == null || u.getName().isBlank()) {
            u.setName(u.getLogin());
        }

        u.setId(id);
        log.debug(
                "Пользователь с электронной почтой {} успешно зарегистрирован.",
                u.getEmail()
        );
        userRepository.put(id, u);
        id++;
        return u;
    }

    @Override
    public User put(User u) throws ObjectNotFoundException {
        Integer id1 = u.getId();
        getByKey(id1);
        userRepository.put(id1, u);
        log.debug(
                "Данные пользователя с электронной почтой {} успешно обновлены.",
                u.getEmail()
        );
        return u;
    }

    public User addFriend(Integer id1, Integer id2) throws ObjectNotFoundException {

        User user = getByKey(id1);
        User friend = getByKey(id2);
        user.addFriend(id2);
        friend.addFriend(id1);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", id1, id2, user.getFriends().size()
        );
        return user;
    }

    public User deleteFriend(Integer id1, Integer id2) throws ObjectNotFoundException {

        User u1 = getByKey(id1);
        User u2 = getByKey(id2);
        boolean b1 = u1.deleteFriend(id2);
        boolean b2 = u2.deleteFriend(id1);
        String exMsg = null;
        if (!b1) {
            exMsg = id2.toString();
            log.warn(
                    "Error! Cannot delete friend with id: {}," +
                            " user doesn't in your friends list!", id2
            );
        }
        if (!b2) {
            exMsg = exMsg + " , " + id1.toString();
            log.warn(
                    "Error! Cannot delete friend with id: {}," +
                            " user doesn't in your friends list!", id1
            );
        }
        if (!b1 || !b2) {
            throw new ObjectNotFoundException("Error! Cannot delete friend with id: "
                    + exMsg + ", user doesn't in your friends list!");
        }
        log.debug(
                "Запрос пользователя под Id: {} на удаление из друзей, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", id1, id2, u1.getFriends().size()
        );
        return u1;
    }

    public Collection<User> getFriendsListById(Integer id1) throws ObjectNotFoundException {

        User user = getByKey(id1);
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", id1, user.getFriends().size()
        );
        return user.getFriends().stream()
                .map(this::getByKey)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriendsList(
            Integer id1, Integer id2) throws ObjectNotFoundException {

        User u1 = getByKey(id1);
        User u2 = getByKey(id2);
        Set<Integer> mutualFriendsSet = new HashSet<>(u1.getFriends());
        mutualFriendsSet.retainAll(u2.getFriends());
        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", id1, id2, mutualFriendsSet.size()
        );
        return mutualFriendsSet.stream()
                .map(this::getByKey)
                .collect(Collectors.toList());
    }
}
