package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService implements ObjectService<Integer, Genre> {
    private final GenreRepository<Integer> repository;
    @Override
    public Collection<Genre> findAll() {
        return repository.findAll();
    }

    @Override
    public Genre getByKey(Integer k) throws ObjectNotFoundException {
        return repository.getByKey(k);
    }

    @Override
    public Genre create(Genre v) {
        return null;
    }

    @Override
    public Genre put(Genre v) {
        return null;
    }
}
