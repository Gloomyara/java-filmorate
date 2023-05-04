package ru.yandex.practicum.filmorate.repository.user.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

import java.util.Map;
import java.util.Set;

public interface FriendsDao {

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId) throws EntityNotFoundException;
}
