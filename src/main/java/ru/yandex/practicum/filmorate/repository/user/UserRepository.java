package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;

public interface UserRepository<K> extends ObjectsRepository<K, User> {
    User addFriend(Integer k1, Integer k2);

    User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException;

    Collection<User> getFriendsListById(Integer k);

    Collection<User> getMutualFriendsList(
            Integer k1, Integer k2);
}
