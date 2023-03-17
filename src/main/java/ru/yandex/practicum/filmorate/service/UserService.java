package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserService extends ObjectService<User> {
    private final InMemoryUserRepository inMemoryUserRepository;

    @Autowired
    public UserService(InMemoryUserRepository inMemoryUserRepository) {
        this.inMemoryUserRepository = inMemoryUserRepository;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> collection = inMemoryUserRepository.findAll();
        log.debug(
                "Запрос списка пользователей успешно выполнен, всего пользователей: {}",
                collection.size()
        );
        return collection;
    }

    @Override
    public User create(User user) throws ObjectAlreadyExistException {

        if (inMemoryUserRepository.get().containsKey(user.getId())) {
            log.warn("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
            throw new ObjectAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        id++;
        log.debug("Пользователь с электронной почтой " +
                user.getEmail() + " успешно зарегистрирован.");
        inMemoryUserRepository.get().put(user.getId(), user);
        return user;
    }

    @Override
    public User put(User user) throws NoSuchElementException {

        if (inMemoryUserRepository.get().containsKey(user.getId())) {
            inMemoryUserRepository.get().put(user.getId(), user);
            log.debug("Данные пользователя с электронной почтой " +
                    user.getEmail() + " успешно обновлены.");
        } else {
            throw new NoSuchElementException("User doesn't exist");
        }
        return user;
    }
}
