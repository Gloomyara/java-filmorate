package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements ObjectService<Integer, Genre> {
    private final GenreRepository<Integer> genreRepository;

    @Override
    public Collection<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getByKey(Integer k) throws ObjectNotFoundException {

        return genreRepository.getByKey(k).orElseThrow(
                () -> new ObjectNotFoundException("Genre with Id: " + k + " not found")
        );
    }

    @Override
    public Genre create(Genre v) throws ObjectAlreadyExistException {
        return genreRepository.create(v);
    }

    @Override
    public Genre put(Genre v) throws ObjectNotFoundException {
        return genreRepository.put(v);
    }
}
