package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService implements ObjectService<Film> {
    private final FilmRepository<Integer> filmRepository;
    private Integer id = 1;

    @Autowired
    public FilmService(FilmRepository<Integer> filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public boolean repositoryContains(Integer id) {
        return filmRepository.getStorage().containsKey(id);
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> collection = filmRepository.findAll();
        log.debug(
                "Запрос списка фильмов успешно выполнен, всего фильмов: {}",
                collection.size()
        );
        return collection;
    }

    @Override
    public Film getById(Integer filmId) throws ObjectNotFoundException {
        if (repositoryContains(filmId)) {
            log.debug(
                    "Запрос фильма по Id: {} успешно выполнен.", filmId
            );
            return filmRepository.getById(filmId);
        } else {
            log.warn(
                    "Фильм c Id: {} не найден.", filmId
            );
            throw new ObjectNotFoundException(
                    "Film with Id: " + filmId + " not found"
            );
        }
    }

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, IllegalArgumentException {

        if (repositoryContains(film.getId())) {
            log.warn(
                    "Фильм под Id: {} уже есть в списке фильмов.",
                    film.getId()
            );
            throw new ObjectAlreadyExistException(
                    "Фильм под Id: " + film.getId() + " уже есть в списке фильмов."
            );
        }
        film.setId(id);
        id++;
        log.debug(
                "Фильм под Id: {} успешно добавлен", id
        );
        filmRepository.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) throws ObjectNotFoundException {

        if (repositoryContains(film.getId())) {
            filmRepository.put(film.getId(), film);
            log.debug(
                    "Данные о фильме {} успешно обновлены", film.getName()
            );
        } else {
            log.warn(
                    "Фильм под Id: {} не найден.",
                    film.getId()
            );
            throw new ObjectNotFoundException(
                    "Film Id: " + film.getId() + " doesn't exist"
            );
        }
        return film;
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (!repositoryContains(filmId)) {
            log.warn(
                    "Фильм под Id: {} не найден.", filmId
            );
            throw new ObjectNotFoundException(
                    "Film Id: " + filmId + " doesn't exist"
            );
        }
        if (!InMemoryUserRepository.contains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User Id:" + userId + " doesn't exist"
            );
        }
        Film film = filmRepository.getById(filmId);
        film.addLike(userId);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                filmId, userId, film.getLikesInfo().size()
        );
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (!repositoryContains(filmId)) {
            log.warn(
                    "Фильм под Id: {} не найден.", filmId
            );
            throw new ObjectNotFoundException(
                    "Film Id: " + filmId + " doesn't exist"
            );
        }
        if (!InMemoryUserRepository.contains(userId)) {
            log.warn(
                    "Пользователь под Id: {} не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "User Id:" + userId + " doesn't exist"
            );
        }
        Film film = filmRepository.getById(filmId);
        if (!film.getLikesInfo().contains(userId)) {
            log.warn(
                    "Ошибка! Не удалось удалить лайк от пользователя под Id: {}" +
                            " лайк не найден.", userId
            );
            throw new ObjectNotFoundException(
                    "Error! Cannot delete user Id: " + userId + " like, user like not found."
            );
        }
        film.deleteLike(userId);
        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                filmId, userId, film.getLikesInfo().size()
        );
        return film;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmRepository.getStorage().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikesInfo().size() - f0.getLikesInfo().size();
    }
}

