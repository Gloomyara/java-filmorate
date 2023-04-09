package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;
import java.util.Map;

public interface UserRepository<K> extends ObjectsRepository<K, User> {
    User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException;

    User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException;

    Collection<User> getFriendsListById(Integer k) throws ObjectNotFoundException;

    Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException;
}
