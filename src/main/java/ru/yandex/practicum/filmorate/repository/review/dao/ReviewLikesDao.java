package ru.yandex.practicum.filmorate.repository.review.dao;

public interface ReviewLikesDao {
    void addLikes(Long id, Long userId, boolean isLike);

    void deleteLikes(Long id, Long userId, boolean isLike);
}
