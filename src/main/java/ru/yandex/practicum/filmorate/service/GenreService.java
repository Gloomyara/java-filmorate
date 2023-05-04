package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.Repository;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

@Service
public class GenreService extends AbstractService<Genre, GenreRepository> {

    protected GenreService(GenreRepository repository) {
        super(repository);
    }
}
