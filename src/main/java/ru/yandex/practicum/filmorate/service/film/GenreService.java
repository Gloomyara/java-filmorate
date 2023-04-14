package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

@Slf4j
@Service
public class GenreService extends ObjectService<Integer, Genre> {

    @Autowired
    public GenreService(GenreRepository<Integer> repository) {
        super(repository, "Genre");
    }

    @Override
    protected ObjectsRepository<Integer, Genre> getRepository() {
        return repository;
    }
}
