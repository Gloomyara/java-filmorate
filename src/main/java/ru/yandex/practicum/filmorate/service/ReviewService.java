package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.repository.review.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.review.dao.impl.ReviewDaoImpl;
import ru.yandex.practicum.filmorate.repository.review.dao.impl.ReviewLikesDaoImpl;
import ru.yandex.practicum.filmorate.repository.user.dao.impl.UserDaoImpl;

import java.util.List;

@Service("reviewService")
@Slf4j
public class ReviewService extends AbstractService<Review, ReviewRepository> {

    private final UserDaoImpl userStorage;
    private final FilmRepository filmStorage;
    private final ReviewLikesDaoImpl likesStorage;


    protected ReviewService(ReviewDaoImpl storage, UserDaoImpl userStorage,
                            FilmDaoImpl filmStorage, ReviewLikesDaoImpl likesStorage) {
        super(storage);
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public Review create(Review t) {
        userStorage.containsOrElseThrow(t.getUserId());
        filmStorage.containsOrElseThrow(t.getFilmId());
        storage.save(t);
        log.info("Создан: {}.", t);
        return findById(t.getId());
    }

    public List<Review> findAllByFilmId(long filmId, int count) throws EntityNotFoundException {
        return storage.findAllByFilmId(filmId, count);
    }

    public Review addLikes(long id, long userId) throws EntityNotFoundException {
        userStorage.containsOrElseThrow(userId);
        likesStorage.addLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review removeLikes(long id, long userId) throws EntityNotFoundException {
        likesStorage.deleteLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review addDislike(long id, long userId) throws EntityNotFoundException {
        userStorage.containsOrElseThrow(userId);
        likesStorage.addLikes(id, userId, false);
        return storage.findById(id).get();
    }

    public Review removeDislikes(long id, long userId) throws EntityNotFoundException {
        likesStorage.deleteLikes(id, userId, false);
        return storage.findById(id).get();
    }
}
