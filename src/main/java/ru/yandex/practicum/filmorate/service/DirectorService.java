package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.repository.Repository;
import ru.yandex.practicum.filmorate.repository.film.DirectorRepository;

@Service
public class DirectorService extends AbstractService<Director, DirectorRepository> {

    protected DirectorService(DirectorRepository repository) {
        super(repository);
    }

}