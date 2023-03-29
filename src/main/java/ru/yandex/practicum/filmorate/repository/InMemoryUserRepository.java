package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository<Integer> {
    private final Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return userStorage.values();
    }

    @Override
    public User getById(Integer id) throws ObjectNotFoundException {
        return userStorage.get(id);
    }

    @Override
    public void put(Integer integer, User user) {
        userStorage.put(integer, user);
    }

    @Override
    public boolean contains(Integer id) {
        return userStorage.containsKey(id);
    }
}

