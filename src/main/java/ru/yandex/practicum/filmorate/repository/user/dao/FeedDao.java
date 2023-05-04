package ru.yandex.practicum.filmorate.repository.user.dao;

import ru.yandex.practicum.filmorate.model.user.Feed;

import java.util.List;

public interface FeedDao {

    void saveUserFeed(Feed feed);

    List<Feed> findAllUserFeed(Long id);
}
