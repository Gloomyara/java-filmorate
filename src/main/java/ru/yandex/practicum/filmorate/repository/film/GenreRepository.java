package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

public interface GenreRepository<K> extends ObjectsRepository<K, Genre> {
}
