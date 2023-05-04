package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.List;

public interface FilmRepository extends Repository<Film> {

    List<Film> findTopByLikes(Long limit, Long genreId, Long year);

    Film addLike(long filmId, long userId, int rate) throws EntityNotFoundException;

    Film deleteLike(long filmId, long userId) throws EntityNotFoundException;

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getFilmRecommendation(Long id);

    List<Film> getDirectorFilmsSortBy(Long directorId, String sortBy);

    List<Film> searchByDirectorOrTitle(String word, String[] locationsForSearch);
}