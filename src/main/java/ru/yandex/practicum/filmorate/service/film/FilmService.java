package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements ObjectService<Integer, Film> {
    private final FilmRepository<Integer> filmRepository;
    private final UserRepository<Integer> userRepository;

    @Override
    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film getByKey(Integer k) throws ObjectNotFoundException {

        return filmRepository.getByKey(k).orElseThrow(
                () -> new ObjectNotFoundException("Film with Id: " + k + " not found")
        );
    }

    @Override
    public Film create(Film v) throws ObjectNotFoundException, ObjectAlreadyExistException {

        if (v.getGenres() != null && v.getGenres().size() > 0) {
            List<Genre> genreIdSet = v.getGenres().stream()
                    .distinct().collect(Collectors.toList());
            v.setGenres(genreIdSet);
        }
        return filmRepository.create(v);
    }

    @Override
    public Film put(Film v) throws ObjectNotFoundException {

        if (v.getGenres() != null && v.getGenres().size() > 0) {
            List<Genre> genreIdSet = v.getGenres().stream()
                    .distinct().collect(Collectors.toList());
            v.setGenres(genreIdSet);
        }
        return filmRepository.put(v);
    }

    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return filmRepository.addLike(k1, k2);
    }

    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return filmRepository.deleteLike(k1, k2);
    }

    public Collection<Film> getPopularFilms(Integer limit) {
        return filmRepository.getPopularFilms(limit);
    }
}
