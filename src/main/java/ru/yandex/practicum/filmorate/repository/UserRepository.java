package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserRepository<K> extends ObjectsRepository<K, User> {
    User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException;

    User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException;

    Map<User, Boolean> getFriendsListById(Integer k) throws ObjectNotFoundException;

    Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException;
}
