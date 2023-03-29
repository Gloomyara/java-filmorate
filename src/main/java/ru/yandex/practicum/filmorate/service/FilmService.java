package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements ObjectService<Film> {
    private final FilmRepository<Integer> filmRepository;
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    @Override
    public boolean repositoryContains(Integer id) {
        return filmRepository.contains(id);
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
        Optional<Integer> optionalFilmId = Optional.ofNullable(filmId);
        try {
            int i = optionalFilmId
                    .filter(this::repositoryContains)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Film with Id: " + filmId + " not found"));
            log.debug(
                    "Запрос фильма по Id: {} успешно выполнен.", i
            );
            return filmRepository.getById(i);
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException {

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
        log.debug(
                "Фильм под Id: {} успешно добавлен", id
        );
        filmRepository.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film put(Film film) throws ObjectNotFoundException {
        Optional<Integer> optionalFilmId = Optional.ofNullable(film.getId());
        try {
            int i = optionalFilmId
                    .filter(this::repositoryContains)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Film with Id: " + film.getId() + " not found"));
            filmRepository.put(i, film);
            log.debug(
                    "Данные о фильме {} успешно обновлены", film.getName()
            );
            return filmRepository.getById(i);
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Film addLike(Integer filmId, Integer userId) {
        Optional<Integer> optionalFilmId = Optional.ofNullable(filmId);
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        try {
            int i = optionalFilmId
                    .filter(this::repositoryContains)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Film Id: " + filmId + " doesn't exist"));
            Film film = filmRepository.getById(i);

            int j = optionalUserId
                    .filter((p) -> userRepository.contains(optionalUserId.orElseThrow()))
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "User Id:" + userId + " doesn't exist"));

            film.addLike(j);
            log.debug(
                    "Фильм под Id: {} получил лайк от пользователя" +
                            " с Id: {}.\n Всего лайков: {}.",
                    i, j, film.getLikesInfo().size()
            );
            return filmRepository.getById(i);
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Optional<Integer> optionalFilmId = Optional.ofNullable(filmId);
        Optional<Integer> optionalUserId = Optional.ofNullable(userId);
        try {
            int i = optionalFilmId
                    .filter(this::repositoryContains)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Film Id: " + filmId + " doesn't exist"));
            Film film = filmRepository.getById(i);

            int j = optionalUserId
                    .filter((p) -> userRepository.contains(
                                    optionalUserId.orElseThrow(
                                            () -> new ObjectNotFoundException(
                                                    "Error! Cannot delete user Id: " + userId
                                                            + " like, user like not found.")
                                    )
                            )
                    )
                    .filter(
                            (p) -> film.getLikesInfo().contains(
                                    optionalUserId.orElseThrow(
                                            () -> new ObjectNotFoundException
                                                    ("Error! Cannot delete user Id: " + userId
                                                            + " like, user like not found.")
                                    )
                            )
                    )
                    .orElseThrow(
                            () -> new ObjectNotFoundException
                                    ("Error! Cannot delete user Id: " + userId
                                            + " like, user like not found.")
                    );

            film.deleteLike(j);
            log.debug(
                    "У фильма под Id: {} удален лайк от пользователя" +
                            " с Id: {}.\n Всего лайков: {}.",
                    i, j, film.getLikesInfo().size()
            );
            return filmRepository.getById(i);
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmRepository.findAll().stream()
                .sorted((f0, f1) -> f1.getLikesInfo().size() - f0.getLikesInfo().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
