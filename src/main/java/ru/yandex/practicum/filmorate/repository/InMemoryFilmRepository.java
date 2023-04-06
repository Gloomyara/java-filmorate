package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmCategory;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryFilmRepository implements FilmRepository<Integer> {
    private final Map<Integer, Film> filmStorage = new HashMap<>();
    private final Map<Integer, FilmCategory> filmCategoryStorage = new HashMap<>();
    private final Map<Integer, FilmRating> filmRatingStorage = new HashMap<>();
    private final Map<Integer, Set<Integer>> likesInfo = new HashMap<>();
    private final UserRepository<Integer> userRepository;

    private Integer id = 1;

    @Override
    public Collection<Film> findAll() {
        Collection<Film> collection = filmStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Film getByKey(Integer k) throws ObjectNotFoundException {
        try {
            Film v = Optional.ofNullable(filmStorage.get(k)).orElseThrow(
                    () -> new ObjectNotFoundException("Film with Id: " + k + " not found")
            );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Film", k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film create(Film v) throws ObjectAlreadyExistException {
        Integer k = v.getId();
        if (filmStorage.containsKey(k)) {
            log.warn(
                    "{} под Id: {}, уже зарегистрирован.",
                    "Film", k
            );
            throw new ObjectAlreadyExistException("Film под Id: " +
                    k + " уже зарегистрирован.");
        }
        v.setId(id);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "Film", k
        );
        filmStorage.put(id, v);
        id++;
        return v;
    }

    @Override
    public Film put(Film v) throws ObjectNotFoundException {
        Integer k = v.getId();
        getByKey(k);
        filmStorage.put(k, v);
        log.debug(
                "Данные {} по Id: {}, успешно обновлены.",
                "Film", k
        );
        return v;
    }

    @Override
    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {

        Film v = getByKey(k1);
        userRepository.getByKey(k2);
        Set<Integer> tempSet = likesInfo.getOrDefault(k1, new HashSet<>());
        tempSet.add(k2);
        v.setRate(tempSet.size());
        likesInfo.put(k1, tempSet);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, v.getRate()
        );
        return v;
    }

    @Override
    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {

        Film v = getByKey(k1);
        userRepository.getByKey(k2);
        Set<Integer> tempSet = likesInfo.getOrDefault(k1, new HashSet<>());
        if (!tempSet.remove(k2)) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    k2
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + k2 + " like, user like not found.");
        }
        v.setRate(tempSet.size());
        likesInfo.put(k1, tempSet);
        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, v.getRate()
        );
        return v;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer i) {
        Collection<Film> collection = filmStorage.values().stream()
                .sorted((f0, f1) -> f1.getRate() - f0.getRate())
                .limit(i)
                .collect(Collectors.toSet());
        log.debug(
                "Запрос списка самых популярных {}'s успешно выполнен, всего {}: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }
}
