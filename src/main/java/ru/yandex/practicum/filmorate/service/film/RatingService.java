package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService implements ObjectService<Integer, Rating> {
    private final RatingRepository<Integer> repository;

    @Override
    public Collection<Rating> findAll() {
        return repository.findAll();
    }

    @Override
    public Rating getByKey(Integer k) throws ObjectNotFoundException {
        return repository.getByKey(k);
    }

    @Override
    public Rating create(Rating v) {
        return null;
    }

    @Override
    public Rating put(Rating v) {
        return null;
    }
}
