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
@Repository
public class InMemoryGenreRepository implements GenreRepository<Integer> {
    private final Map<Integer, Genre> genreStorage = new HashMap<>();

    private Integer id = 1;

    @Override
    public Collection<Genre> findAll() {
        Collection<Genre> collection = genreStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}: {}",
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
    public Genre create(Genre genre) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public Genre put(Genre genre) throws ObjectNotFoundException {
        return null;
    }
}
