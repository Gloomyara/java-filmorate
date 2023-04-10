package ru.yandex.practicum.filmorate.repository.film.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository("InMemoryFilmRepository")
public class InMemoryFilmRepository implements FilmRepository<Integer> {
    private final Map<Integer, Film> filmStorage = new HashMap<>();
    private final Map<Integer, Set<Integer>> likesInfo = new HashMap<>();

    private Integer id = 1;

    @Override
    public void containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        if (!filmStorage.containsKey(k)) {
            throw new ObjectNotFoundException("Film with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> collection = filmStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Film", "Film", collection.size()
        );
        return collection;
    }

    @Override
    public Optional<Film> getByKey(Integer k) {
        Optional<Film> optV = Optional.ofNullable(filmStorage.get(k));
        if (optV.isPresent()) {
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Film", k
            );
            return optV;
        }
        log.warn("Film with Id: {} not found", k);
        return Optional.empty();
    }

    @Override
    public Film create(Film v) throws ObjectAlreadyExistException {
        Integer k = v.getId();
        if (filmStorage.containsKey(k)) {
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Film", k
            );
            throw new ObjectAlreadyExistException("Film Id: " + k + " should be null," +
                    " Id генерируется автоматически.");
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
        containsOrElseThrow(k);
        filmStorage.put(k, v);
        log.debug(
                "Данные {} по Id: {}, успешно обновлены.",
                "Film", k
        );
        return v;
    }

    @Override
    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {

        Film v = getByKey(k1).orElseThrow(
                () -> new ObjectNotFoundException("Film with Id: " + k1 + " not found")
        );
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

        Film v = getByKey(k1).orElseThrow(
                () -> new ObjectNotFoundException("Film with Id: " + k1 + " not found")
        );
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
