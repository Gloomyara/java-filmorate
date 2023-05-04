package ru.yandex.practicum.filmorate.repository.film.dao;

public interface FilmLikesDao {
    boolean addLike(long filmId, long userId, int rate);

    boolean deleteLike(long filmId, long userId);
}
