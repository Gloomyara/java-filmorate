package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserService extends ObjectService<User> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> collection = userRepository.findAll();
        log.debug(
                "Запрос списка пользователей успешно выполнен, всего пользователей: {}",
                collection.size()
        );
        return collection;
    }

    @Override
    public User create(User user) throws ValidationException, ObjectAlreadyExistException {
        violations = validator.validate(user);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                log.warn(violation.getMessage());
            }
            throw new ValidationException("User validation fail");
        }
        if (userRepository.get().containsKey(user.getId())) {
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
        userRepository.get().put(user.getId(), user);
        return user;
    }

    @Override
    public User put(User user) throws ValidationException, NoSuchElementException {
        violations = validator.validate(user);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                log.warn(violation.getMessage());
            }
            throw new ValidationException("User validation fail");
        }
        if (userRepository.get().containsKey(user.getId())) {
            userRepository.get().put(user.getId(), user);
            log.debug("Данные пользователя с электронной почтой " +
                    user.getEmail() + " успешно обновлены.");
        } else {
            throw new NoSuchElementException("User doesn't exist");
        }
        return user;
    }
}
