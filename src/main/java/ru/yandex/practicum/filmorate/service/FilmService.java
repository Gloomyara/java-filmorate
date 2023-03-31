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
public class FilmService implements ObjectService<Integer, Film> {

    private final FilmRepository<Integer> filmRepository;
    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    public boolean userRepositoryContainsKey(Integer k) {
        return userRepository.getByKey(k) != null;
    }

    @Override
    public boolean repositoryContainsKey(Integer k) {
        return filmRepository.getByKey(k) != null;
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
    public Film getByKey(Integer id1) throws ObjectNotFoundException {
        try {
            Film f = Optional.ofNullable(filmRepository.getByKey(id1)).orElseThrow(
                    () -> new ObjectNotFoundException("Film with Id: " + id1 + " not found")
            );
            log.debug(
                    "Запрос фильма по Id: {} успешно выполнен.",
                    id1
            );
            return f;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film create(Film f) throws ObjectAlreadyExistException {

        if (repositoryContainsKey(f.getId())) {
            log.warn(
                    "Фильм под Id: {} уже есть в списке фильмов.",
                    f.getId()
            );
            throw new ObjectAlreadyExistException("Фильм под Id: "
                    + f.getId() + " уже есть в списке фильмов.");
        }
        f.setId(id);
        log.debug(
                "Фильм под Id: {} успешно добавлен",
                id
        );
        filmRepository.put(id, f);
        id++;
        return f;
    }

    @Override
    public Film put(Film f) throws ObjectNotFoundException {

        Integer id1 = f.getId();
        getByKey(id1);

        filmRepository.put(id1, f);
        log.debug(
                "Данные о фильме {} успешно обновлены",
                f.getName()
        );
        return f;
    }

    public Film addLike(Integer id1, Integer id2) {

        Film f = getByKey(id1);
        if (!userRepositoryContainsKey(id2)) {
            log.warn(
                    "User Id: {} doesn't exist",
                    id2
            );
            throw new ObjectNotFoundException("User Id:" + id2 + " doesn't exist");
        }
        f.addLike(id2);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                id1, id2, f.getLikesInfo().size()
        );
        return f;
    }

    public Film deleteLike(Integer id1, Integer id2) {

        Film f = getByKey(id1);
        if (!userRepositoryContainsKey(id2) || !f.deleteLike(id2)) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    id2
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + id2 + " like, user like not found.");
        }

        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                id1, id2, f.getLikesInfo().size()
        );
        return f;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmRepository.findAll().stream()
                .sorted((f0, f1) -> f1.getLikesInfo().size() - f0.getLikesInfo().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
