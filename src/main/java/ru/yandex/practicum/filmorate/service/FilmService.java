package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import javax.validation.ConstraintViolation;
import java.util.NoSuchElementException;

@Slf4j
public class FilmService extends ObjectService<Film> {

    public FilmService(ObjectsRepository<Film> repository) {
        super(repository);
    }

    @Override
    public Film create(Film film) throws FilmValidationException, FilmAlreadyExistException {
        violations = validator.validate(film);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Film> violation : violations) {
                log.warn(violation.getMessage());
            }
            throw new FilmValidationException("Film validation fail");
        }
        if (repository.get().containsKey(film.getName())) {
            log.warn("Фильм под названием " +
                    film.getName() + " уже есть в списке фильмов.");
            throw new FilmAlreadyExistException("Фильм под названием " +
                    film.getName() + " уже есть в списке фильмов.");
        }
        log.debug("Фильм под названием " +
                film.getName() + " успешно добавлен");
        repository.get().put(film.getName(), film);
        return film;
    }

    @Override
    public Film put(Film film) throws FilmValidationException, NoSuchElementException {
        violations = validator.validate(film);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Film> violation : violations) {
                log.warn(violation.getMessage());
            }
            throw new FilmValidationException("Film validation fail");
        }
        if (repository.get().containsKey(film.getName())) {
            repository.get().put(film.getName(), film);
            log.debug("Данные о фильме " +
                    film.getName() + " успешно обновлены");
        } else {
            throw new NoSuchElementException("Film doesn't exist");
        }
        return film;
    }
}
