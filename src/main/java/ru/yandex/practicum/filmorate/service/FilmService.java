package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@Qualifier("filmService")
public class FilmService extends AbstractService<Film, FilmRepository> {

    private final UserRepository userRepository;

    public FilmService(@Qualifier("filmDbStorage") FilmRepository filmRepository, UserRepository userRepository) {
        super(filmRepository);
        this.userRepository = userRepository;
    }

    public void addLike(Long filmId, Long userId, int rate) {
        storage.containsOrElseThrow(filmId);
        userRepository.containsOrElseThrow(userId);
        storage.addLike(filmId, userId, rate);
    }

    public Film removeLike(Long filmId, Long userId) {
        storage.containsOrElseThrow(filmId);
        userRepository.containsOrElseThrow(userId);
        return storage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(Long count, Long genreId, Long year) {
        var films = storage.findTopByLikes(count, genreId, year);
        log.info("Топ {} фильм(ов) по лайкам: {}.", count, films.stream().map(Entity::getId).toArray());
        return films;
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userRepository.containsOrElseThrow(userId);
        userRepository.containsOrElseThrow(friendId);
        var films = storage.getCommonFilms(userId, friendId);
        log.info("Получение общих фильмов для user с id {} и друга с id {}", userId, friendId);
        return films;
    }

    public List<Film> getDirectorFilmsSortBy(Long directorId, String sortBy) {
        var films = storage.getDirectorFilmsSortBy(directorId, sortBy);
        log.info("Топ режисера {} по {}: {}", directorId, sortBy, films.stream().map(Entity::getId).toArray());
        return films;
    }

    public List<Film> searchByDirectorOrTitle(String word, String location) {
        String[] locationsForSearch = location.split(",");
        if ((locationsForSearch[0].equals("director") || locationsForSearch[0].equals("title")) &&
                (locationsForSearch.length == 1 || locationsForSearch.length == 2 &&
                (locationsForSearch[1].equals("director") || locationsForSearch[1].equals("title")))) {
            word = word.toLowerCase();
            List<Film> films = storage.searchByDirectorOrTitle(word, locationsForSearch);
            log.info("Поиск {} по {}: {}", word, locationsForSearch, films.stream().map(Entity::getId).toArray());
            return films;
        } else {
            throw new IncorrectParameterException("Некорректные параметры ", "поиска");
        }
    }
}