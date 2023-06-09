package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;

public interface FilmRepository<K> extends ObjectsRepository<K, Film> {

    Film addLike(Integer k1, Integer k2) throws ObjectNotFoundException;

    Film deleteLike(Integer k1, Integer k2) throws ObjectNotFoundException;

    Collection<Film> getPopularFilms(Integer i);
}
