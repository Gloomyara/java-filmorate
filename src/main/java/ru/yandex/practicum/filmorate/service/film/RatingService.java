package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

@Slf4j
@Service
public class RatingService extends ObjectService<Integer, Rating> {

    @Autowired
    public RatingService(RatingRepository<Integer> repository) {
        super(repository, "Rating");
    }
}
