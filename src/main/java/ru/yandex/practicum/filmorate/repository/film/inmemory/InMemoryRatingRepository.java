package ru.yandex.practicum.filmorate.repository.film.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository("InMemoryRatingRepository")
public class InMemoryRatingRepository implements RatingRepository<Integer> {
    private final Map<Integer, Rating> ratingStorage = new HashMap<>();
    private Integer id = 1;

    @Override
    public void containsOrElseThrow(Integer k) {
        if (!ratingStorage.containsKey(k)) {
            throw new ObjectNotFoundException("Rating with Id: " + k + " not found");
        }
    }

    @Override
    public Collection<Rating> findAll() {
        Collection<Rating> collection = ratingStorage.values();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}: {}'s",
                "Rating", "Rating", collection.size()
        );
        return collection;
    }

    @Override
    public Rating getByKey(Integer k) throws ObjectNotFoundException {
        try {
            Rating v = Optional.ofNullable(ratingStorage.get(k)).orElseThrow(
                    () -> new ObjectNotFoundException("Rating with Id: " + k + " not found")
            );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "Rating", k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Rating create(Rating v) throws ObjectAlreadyExistException {
        Integer k = v.getId();
        if (ratingStorage.containsKey(k)) {
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "Rating", k
            );
            throw new ObjectAlreadyExistException("Rating Id: " + k + " should be null," +
                    " Id генерируется автоматически.");
        }
        v.setId(id);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "Rating", k
        );
        ratingStorage.put(id, v);
        id++;
        return v;
    }

    @Override
    public Rating put(Rating v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        ratingStorage.put(k, v);
        log.debug(
                "Данные {} по Id: {}, успешно обновлены.",
                "Rating", k
        );
        return v;
    }
}
