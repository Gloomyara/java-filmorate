package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService implements ObjectService<Integer, Rating> {
    private final RatingRepository<Integer> ratingRepository;

    @Override
    public Collection<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getByKey(Integer k) throws ObjectNotFoundException {

        return ratingRepository.getByKey(k).orElseThrow(
                () -> new ObjectNotFoundException("Rating with Id: " + k + " not found")
        );
    }

    @Override
    public Rating create(Rating v) throws ObjectAlreadyExistException {
        return ratingRepository.create(v);
    }

    @Override
    public Rating put(Rating v) throws ObjectNotFoundException {
        return ratingRepository.put(v);
    }
}
