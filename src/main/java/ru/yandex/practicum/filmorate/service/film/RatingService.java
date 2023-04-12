package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

@Service
public class RatingService extends ObjectService<Integer, Rating> {

    @Autowired
    public RatingService(RatingRepository<Integer> repository) {
        super(repository, "Rating");
    }

    @Override
    protected ObjectsRepository<Integer, Rating> getRepository() {
        return repository;
    }
}
