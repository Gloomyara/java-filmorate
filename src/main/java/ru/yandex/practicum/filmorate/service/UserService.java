package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements ObjectService<Integer, User> {

    private final UserRepository<Integer> repository;

    @Override
    public Collection<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User getByKey(Integer k) throws ObjectNotFoundException {
        return repository.getByKey(k);
    }

    @Override
    public User create(User v) throws ObjectAlreadyExistException {
        return repository.create(v);
    }

    @Override
    public User put(User v) throws ObjectNotFoundException {
        return repository.put(v);
    }

    public User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        return repository.addFriend(k1, k2);
    }

    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        return repository.deleteFriend(k1, k2);
    }

    public Map<User, Boolean> getFriendsListById(Integer k1) throws ObjectNotFoundException {
        return repository.getFriendsListById(k1);
    }

    public Collection<User> getMutualFriendsList(
            Integer k1, Integer k2) throws ObjectNotFoundException {
        return repository.getMutualFriendsList(k1, k2);
    }
}
