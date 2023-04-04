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
    public User getByKey(Integer k) throws ObjectNotFoundException {
        if (userStorage.containsKey(k)) {
            return userStorage.get(k);
        }
        return null;
    }

    @Override
    public void put(Integer k, User v) {
        userStorage.put(k, v);
    }
}
