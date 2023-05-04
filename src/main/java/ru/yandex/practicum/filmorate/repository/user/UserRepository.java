package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User> {

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId) throws EntityNotFoundException;

    List<User> getFriendsListById(Long userId);

    List<User> getMutualFriendsList(Long userId, Long friendId);
}
