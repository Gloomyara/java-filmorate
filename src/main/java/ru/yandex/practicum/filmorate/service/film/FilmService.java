package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final UserRepository<Integer> userRepository;
    private final GenreRepository<Integer> genreRepository;
    private final RatingRepository<Integer> ratingRepository;

    @Override
    public Collection<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film getByKey(Integer k) {
        return repository.getByKey(k);
    }

    @Override
    public Film create(Film v) {
        int ratingId = v.getRatingId();
        ratingRepository.getByKey(ratingId);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            genreRepository.getByKey(i);
        }
        return repository.create(v);
    }

    @Override
    public Film put(Film v) {
        int ratingId = v.getRatingId();
        ratingRepository.getByKey(ratingId);
        Set<Integer> genreIdSet = v.getGenreIdSet();
        for (int i : genreIdSet) {
            genreRepository.getByKey(i);
        }
        return repository.put(v);
    }

    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.getByKey(k2);
        return repository.addLike(k1, k2);
    }

    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.getByKey(k2);
        return repository.deleteLike(k1, k2);
    }

    public Collection<Film> getPopularFilms(Integer limit) {
        return repository.getPopularFilms(limit);
    }
}
