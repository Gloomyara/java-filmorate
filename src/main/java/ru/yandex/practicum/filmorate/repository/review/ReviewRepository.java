package ru.yandex.practicum.filmorate.repository.review;

import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.List;

public interface ReviewRepository extends Repository<Review> {
    List<Review> findAllByFilmId(long filmId, int count);
}
