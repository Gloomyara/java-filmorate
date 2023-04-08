package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService implements ObjectService<Integer, Film> {
    private final FilmRepository<Integer> repository;
    private final GenreRepository<Integer> genreRepository;
    private final RatingRepository<Integer> ratingRepository;
    private final UserRepository<Integer> userRepository;

    @Override
    public Collection<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film getByKey(Integer k) throws ObjectNotFoundException {
        return repository.getByKey(k);
    }

    @Override
    public Film create(Film v) throws ObjectNotFoundException, ObjectAlreadyExistException {
        int ratingId = v.getRatingId();
        ratingRepository.containsOrElseThrow(ratingId);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            genreRepository.containsOrElseThrow(i);
        }
        return repository.create(v);
    }

    @Override
    public Film put(Film v) throws ObjectNotFoundException {
        int ratingId = v.getRatingId();
        ratingRepository.containsOrElseThrow(ratingId);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            genreRepository.containsOrElseThrow(i);
        }
        return repository.put(v);
    }

    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return repository.addLike(k1, k2);
    }

    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return repository.deleteLike(k1, k2);
    }

    public Collection<Film> getPopularFilms(Integer limit) {
        return repository.getPopularFilms(limit);
    }
}
