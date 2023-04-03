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
public class UserService extends ObjectService<UserRepository<Integer>, Integer, User> {

    private Integer id = 1;

    public UserService(UserRepository<Integer> repository) {
        super(repository);
        super.className = "User";
    }

    @Override
    protected Integer getKey(User user) {
        return user.getId();
    }

    @Override
    protected Integer objectPreparation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        return id++;
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
