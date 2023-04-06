package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository<K> extends ObjectsRepository<K, Film> {

    Film addLike(Integer k1, Integer k2);

    Film deleteLike(Integer k1, Integer k2);

    Collection<Film> getPopularFilms(Integer i);
}
