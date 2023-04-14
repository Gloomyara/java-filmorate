package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

@Slf4j
@Service
public class FilmService extends ObjectService<Integer, Film> {
    private final UserRepository<Integer> userRepository;

    @Autowired
    public FilmService(FilmRepository<Integer> repository, UserRepository<Integer> userRepository) {
        super(repository, "Film");
        this.userRepository = userRepository;
    }

    public Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return getRepository().addLike(k1, k2);
    }

    public Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException {
        userRepository.containsOrElseThrow(k2);
        return getRepository().deleteLike(k1, k2);
    }

    public Collection<Film> getPopularFilms(Integer limit) {
        return getRepository().getPopularFilms(limit);
    }

    @Override
    protected FilmRepository<Integer> getRepository() {
        return (FilmRepository<Integer>) repository;
    }
}
