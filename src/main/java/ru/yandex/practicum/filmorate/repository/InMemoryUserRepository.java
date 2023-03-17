package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements ObjectsRepository<Integer, User> {
    protected final Map<Integer, User> userObjects = new HashMap<>();
    @Override
    public Collection<User> findAll() {
        return userObjects.values();
    }

    @Override
    public Map<Integer, User> get() {
        return userObjects;
    }
}
