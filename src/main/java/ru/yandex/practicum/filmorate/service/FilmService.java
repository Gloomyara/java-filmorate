package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class FilmService extends ObjectService<Film> {
    private final InMemoryFilmRepository inMemoryFilmRepository;

    @Autowired
    public FilmService(InMemoryFilmRepository inMemoryFilmRepository) {
        this.inMemoryFilmRepository = inMemoryFilmRepository;
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> collection = inMemoryFilmRepository.findAll();
        log.debug(
                "Запрос списка фильмов успешно выполнен, всего фильмов: {}",
                collection.size()
        );
        return collection;
    }

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, IllegalArgumentException {

        if (inMemoryFilmRepository.get().containsKey(film.getId())) {
            log.warn("Фильм под названием " +
                    film.getName() + " уже есть в списке фильмов.");
            throw new ObjectAlreadyExistException("Фильм под названием " +
                    film.getName() + " уже есть в списке фильмов.");
        }
        film.setId(id);
        id++;
        log.debug("Фильм под названием " +
                film.getName() + " успешно добавлен");
        inMemoryFilmRepository.get().put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) throws NoSuchElementException {

        if (inMemoryFilmRepository.get().containsKey(film.getId())) {
            inMemoryFilmRepository.get().put(film.getId(), film);
            log.debug("Данные о фильме " +
                    film.getName() + " успешно обновлены");
        } else {
            throw new NoSuchElementException("Film doesn't exist");
        }
        return film;
    }
}
