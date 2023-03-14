package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.NoSuchElementException;

@Slf4j
public class UserService extends ObjectService<User> {

    public UserService(ObjectsRepository<User> repository) {
        super(repository);
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
        if (repository.get().containsKey(user.getEmail())) {
            log.warn("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
            throw new ObjectAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        log.debug("Пользователь с электронной почтой " +
                user.getEmail() + " успешно зарегистрирован.");
        repository.get().put(user.getEmail(), user);
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
        if (repository.get().containsKey(user.getEmail())) {
            repository.get().put(user.getEmail(), user);
            log.debug("Данные пользователя с электронной почтой " +
                    user.getEmail() + " успешно обновлены.");
        } else {
            throw new NoSuchElementException("User doesn't exist");
        }
        return user;
    }
}
