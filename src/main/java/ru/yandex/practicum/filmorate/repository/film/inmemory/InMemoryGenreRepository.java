package ru.yandex.practicum.filmorate.repository.film.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository("InMemoryGenreRepository")
public class InMemoryGenreRepository implements GenreRepository<Integer> {
    private final Map<Integer, Genre> genreStorage = new HashMap<>();

    private Integer id = 1;

    @Override
    public void containsOrElseThrow(Integer k) {
        if (!genreStorage.containsKey(k)) {
            throw new ObjectNotFoundException("Genre with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<Genre> findAll() {
        Collection<Genre> collection = genreStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Genre", "Genre", collection.size()
        );
        return collection;
    }

    @Override
    public Genre getByKey(Integer k) throws ObjectNotFoundException {
        try {
            Genre v = Optional.ofNullable(genreStorage.get(k)).orElseThrow(
                    () -> new ObjectNotFoundException("Genre with Id: " + k + " not found")
            );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Genre", k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Genre create(Genre v) throws ObjectAlreadyExistException {
        Integer k = v.getId();
        if (genreStorage.containsKey(k)) {
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Genre", k
            );
            throw new ObjectAlreadyExistException("Genre Id: " + k + " should be null," +
                    " Id генерируется автоматически.");
        }
        v.setId(id);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "Genre", k
        );
        genreStorage.put(id, v);
        id++;
        return v;
    }

    @Override
    public Genre put(Genre v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        genreStorage.put(k, v);
        log.debug(
                "Данные {} по Id: {}, успешно обновлены.",
                "Genre", k
        );
        return v;
    }
}
