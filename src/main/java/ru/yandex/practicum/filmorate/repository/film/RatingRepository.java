package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

public interface RatingRepository<K> extends ObjectsRepository<K, Rating> {
}
