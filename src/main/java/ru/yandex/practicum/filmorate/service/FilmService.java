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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements ObjectService<Film> {

    private final FilmRepository<Integer> filmRepository;
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    public boolean userRepositoryContainsKey(Integer id) {
        return userRepository.getByKey(id) != null;
    }

    @Override
    public boolean repositoryContainsKey(Integer id) {
        return filmRepository.getByKey(id) != null;
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
    public Film getByKey(Integer filmId) throws ObjectNotFoundException {
        try {
            Film film = Optional.ofNullable(filmRepository.getByKey(filmId)).orElseThrow(
                    () -> new ObjectNotFoundException("Film with Id: " + filmId + " not found")
            );
            log.debug(
                    "Запрос фильма по Id: {} успешно выполнен.",
                    filmId
            );
            return film;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException {

        if (repositoryContainsKey(film.getId())) {
            log.warn(
                    "Фильм под Id: {} уже есть в списке фильмов.",
                    film.getId()
            );
            throw new ObjectAlreadyExistException("Фильм под Id: "
                    + film.getId() + " уже есть в списке фильмов.");
        }
        film.setId(id);
        log.debug(
                "Фильм под Id: {} успешно добавлен",
                id
        );
        filmRepository.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film put(Film film) throws ObjectNotFoundException {

        Integer filmId = film.getId();
        getByKey(filmId);

        filmRepository.put(filmId, film);
        log.debug(
                "Данные о фильме {} успешно обновлены",
                film.getName()
        );
        return film;
    }

    public Film addLike(Integer filmId, Integer userId) {

        Film film = getByKey(filmId);
        if (!userRepositoryContainsKey(userId)) {
            log.warn(
                    "User Id: {} doesn't exist",
                    userId
            );
            throw new ObjectNotFoundException("User Id:" + userId + " doesn't exist");
        }
        film.addLike(userId);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                filmId, userId, film.getLikesInfo().size()
        );
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {

        Film film = getByKey(filmId);
        if (!userRepositoryContainsKey(userId) || !film.deleteLike(userId)) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    userId
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + userId + " like, user like not found.");
        }

        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                filmId, userId, film.getLikesInfo().size()
        );
        return film;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmRepository.findAll().stream()
                .sorted((f0, f1) -> f1.getLikesInfo().size() - f0.getLikesInfo().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
