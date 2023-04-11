package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
public class UserService extends ObjectService<Integer, User> {

    private final UserRepository<Integer> userRepository;

    @Autowired
    public UserService(UserRepository<Integer> repository) {
        super(repository, "User");
        this.userRepository = repository;
    }

    public User addFriend(Integer k1, Integer k2)
            throws ObjectNotFoundException, ObjectAlreadyExistException {

        if (Objects.equals(k1, k2)) {
            throw new ObjectAlreadyExistException(
                    "Ошибка! Нельзя добавить в друзья самого себя!");
        }
        userRepository.containsOrElseThrow(k1);
        userRepository.containsOrElseThrow(k2);
        return userRepository.addFriend(k1, k2);
    }

    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k1);
        userRepository.containsOrElseThrow(k2);
        return userRepository.deleteFriend(k1, k2);
    }

    public Collection<User> getFriendsListById(Integer k1) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k1);
        return userRepository.getFriendsListById(k1);
    }

    public Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k1);
        userRepository.containsOrElseThrow(k2);
        return userRepository.getMutualFriendsList(k1, k2);
    }
}
