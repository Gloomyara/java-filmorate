package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends ObjectService<Integer, User> {

    private Integer id = 1;

    public UserService(UserRepository<Integer> repository) {
        super(repository, "User");
    }

    @Override
    protected Integer getKey(User v) {
        return v.getId();
    }

    @Override
    protected Integer objectPreparation(User v) {
        if (v.getName() == null || v.getName().isBlank()) {
            v.setName(v.getLogin());
        }
        v.setId(id);
        return id++;
    }

    public User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException {

        User v1 = getByKey(k1);
        User v2 = getByKey(k2);
        v1.addFriend(k2);
        v2.addFriend(k1);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k1, k2, v1.getFriends().size()
        );
        return v1;
    }

    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {

        User v1 = getByKey(k1);
        User v2 = getByKey(k2);
        boolean b1 = v1.deleteFriend(k2);
        boolean b2 = v2.deleteFriend(k1);
        String exMsg = null;
        if (!b1) {
            exMsg = k2.toString();
            log.warn(
                    "Error! Cannot delete friend with id: {}," +
                            " user doesn't in your friends list!", k2
            );
        }
        if (!b2) {
            exMsg = exMsg + " , " + k1.toString();
            log.warn(
                    "Error! Cannot delete friend with id: {}," +
                            " user doesn't in your friends list!", k1
            );
        }
        if (!b1 || !b2) {
            throw new ObjectNotFoundException("Error! Cannot delete friend with id: "
                    + exMsg + ", user doesn't in your friends list!");
        }
        log.debug(
                "Запрос пользователя под Id: {} на удаление из друзей, " +
                        "пользователя Id: {}, успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k1, k2, v1.getFriends().size()
        );
        return v1;
    }

    public Collection<User> getFriendsListById(Integer k1) throws ObjectNotFoundException {

        User user = getByKey(k1);
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k1, user.getFriends().size()
        );
        return user.getFriends().stream()
                .map(this::getByKey)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException {

        User v1 = getByKey(k1);
        User v2 = getByKey(k2);
        Set<Integer> mutualFriendsSet = new HashSet<>(v1.getFriends());
        mutualFriendsSet.retainAll(v2.getFriends());
        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", k1, k2, mutualFriendsSet.size()
        );
        return mutualFriendsSet.stream()
                .map(this::getByKey)
                .collect(Collectors.toList());
    }
}
