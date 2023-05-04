package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.Repository;
import ru.yandex.practicum.filmorate.repository.film.MPARatingRepository;

@Service
public class MPAService extends AbstractService<MPARating, MPARatingRepository> {

    protected MPAService(MPARatingRepository repository) {
        super(repository);
    }
}
