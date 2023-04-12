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


    @Autowired
    public UserService(UserRepository<Integer> repository) {
        super(repository, "User");
    }

    public User addFriend(Integer k1, Integer k2)
            throws ObjectNotFoundException, ObjectAlreadyExistException {

        if (Objects.equals(k1, k2)) {
            log.warn("Ошибка! Нельзя запросить общий список друзей с самим собой!");
            throw new ObjectAlreadyExistException(
                    "Ошибка! Нельзя добавить в друзья самого себя!");
        }
        return getRepository().addFriend(k1, k2);
    }

    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        return getRepository().deleteFriend(k1, k2);
    }

    public Collection<User> getFriendsListById(Integer k1) throws ObjectNotFoundException {
        return getRepository().getFriendsListById(k1);
    }

    public Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException, ObjectAlreadyExistException {
        if (Objects.equals(k1, k2)) {
            log.warn("Ошибка! Нельзя запросить общий список друзей с самим собой!");
            throw new ObjectAlreadyExistException(
                    "Ошибка! Нельзя запросить общий список друзей с самим собой!");
        }
        return getRepository().getMutualFriendsList(k1, k2);
    }

    @Override
    protected UserRepository<Integer> getRepository() {
        return (UserRepository<Integer>) repository;
    }
}
