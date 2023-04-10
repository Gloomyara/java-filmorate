package ru.yandex.practicum.filmorate.repository.user.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository("InMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository<Integer> {
    private final Map<Integer, User> userStorage = new HashMap<>();
    private final Map<Integer, Map<Integer, Boolean>> friendsInfo = new HashMap<>();
    private Integer id = 1;

    @Override
    public void containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        if (!userStorage.containsKey(k) || !friendsInfo.containsKey(k)) {
            throw new ObjectNotFoundException("User with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> collection = userStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "User", "User", collection.size()
        );
        return collection;
    }

    @Override
    public Optional<User> getByKey(Integer k) {

        Optional<User> optV = Optional.ofNullable(userStorage.get(k));
        if (optV.isPresent()) {
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "User", k
            );
            return optV;
        }
        log.warn("User with Id: {} not found", k);
        return Optional.empty();
    }

    @Override
    public User create(User v) throws ObjectAlreadyExistException {
        Integer k = v.getId();
        if (userStorage.containsKey(k)) {
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "User", k
            );
            throw new ObjectAlreadyExistException("User Id: " + k + " should be null," +
                    " Id генерируется автоматически.");
        }
        if (v.getName() == null || v.getName().isBlank()) {
            v.setName(v.getLogin());
        }
        v.setId(id);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "User", id
        );
        userStorage.put(id, v);
        friendsInfo.put(id, new HashMap<>());
        id++;
        return v;
    }

    @Override
    public User put(User v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        userStorage.put(k, v);
        log.debug(
                "Данные {} по Id: {}, успешно обновлены.",
                "User", k
        );
        return v;
    }

    @Override
    public User addFriend(Integer k1, Integer k2) {
        User v = getByKey(k1).orElseThrow();
        var tempMap1 = friendsInfo.getOrDefault(k1, new HashMap<>());
        var tempMap2 = friendsInfo.getOrDefault(k2, new HashMap<>());
        if (tempMap2.containsKey(k1)) {
            tempMap1.put(k2, true);
            tempMap2.put(k1, true);
            friendsInfo.put(k1, tempMap1);
            friendsInfo.put(k2, tempMap2);
            log.debug(
                    "Подтверждение пользователем под Id:{} запроса на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнено!\n" +
                            "Всего друзей в списке: {}.", k1, k2,
                    (int) tempMap1.keySet().stream().filter(tempMap1::get).count()
            );
            return v;
        }
        tempMap1.put(k2, false);
        friendsInfo.put(k1, tempMap1);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя под Id: {}, успешно выполнен!", k1, k2
        );
        return v;
    }

    @Override
    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        User v = getByKey(k1).orElseThrow();
        try {
            var tempMap1 = friendsInfo.get(k1);
            if (tempMap1.get(k2)) {
                var tempMap2 = friendsInfo.get(k2);
                Optional.ofNullable(
                        tempMap2.remove(k1)).orElseThrow(() ->
                        new ObjectNotFoundException(
                                "Error! Cannot delete User Id: " + k2 + " friend with id: "
                                        + k1 + ", user doesn't in your friends list!")
                );
                friendsInfo.put(k2, tempMap2);
            }
            Optional.ofNullable(tempMap1.remove(k2)).orElseThrow(() ->
                    new ObjectNotFoundException(
                            "Error! Cannot delete User Id: " + k1 + "friend with id: "
                                    + k2 + ", user doesn't in your friends list!")
            );
            friendsInfo.put(k1, tempMap1);
            log.debug(
                    "Запрос пользователя под Id: {} на удаление из друзей, " +
                            "пользователя под Id: {}, успешно выполнен!",
                    k1, k2
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Collection<User> getFriendsListById(Integer k) {

        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k, friendsInfo.get(k).size()
        );
        return friendsInfo.get(k).keySet().stream()
                .map(this::getByKey)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) {

        Set<Integer> mutualFriendsSet = friendsInfo.get(k1).keySet().stream()
                .filter(friendsInfo.get(k1)::get)
                .collect(Collectors.toSet());

        mutualFriendsSet.retainAll(friendsInfo.get(k2).keySet().stream()
                .filter(friendsInfo.get(k2)::get)
                .collect(Collectors.toSet()));

        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", k1, k2, mutualFriendsSet.size()
        );
        return mutualFriendsSet.stream()
                .map(this::getByKey)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
