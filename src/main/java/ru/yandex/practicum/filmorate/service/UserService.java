package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.film.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.dao.impl.FeedDaoImpl;

import java.util.List;

@Slf4j
@Service("userService")
public class UserService extends AbstractService<User, UserRepository> {
    private final FeedDaoImpl feedStorage;
    private final FilmDaoImpl filmStorage;

    public UserService(UserRepository storage,
                       FilmDaoImpl filmStorage,
                       FeedDaoImpl feedStorage) {
        super(storage);
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
    }

    @Override
    public User create(User user) throws EntityAlreadyExistException {
        if (user.getId() != null) {
            long i = user.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "User", i
            );
            throw new EntityAlreadyExistException("User Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        nameIsLoginIfNameIsNull(user);
        return super.create(user);
    }

    @Override
    public User update(User user) throws EntityNotFoundException {
        storage.containsOrElseThrow(user.getId());
        nameIsLoginIfNameIsNull(user);
        return storage.update(user);
    }

    public User addFriend(Long id, Long friendId) throws EntityNotFoundException {
        storage.containsOrElseThrow(id);
        storage.containsOrElseThrow(friendId);
        return storage.addFriend(id, friendId);
    }

    public User removeFriend(Long id, Long friendId) throws EntityNotFoundException {
        storage.containsOrElseThrow(id);
        storage.containsOrElseThrow(friendId);
        return storage.deleteFriend(id, friendId);
    }

    public List<User> findUserFriends(Long id) {
        storage.containsOrElseThrow(id);
        return storage.getFriendsListById(id);
    }

    public List<User> findMutualFriends(Long id, Long otherId) throws EntityNotFoundException {
        storage.containsOrElseThrow(id);
        storage.containsOrElseThrow(otherId);
        return storage.getMutualFriendsList(id, otherId);
    }

    public List<Film> getFilmRecommendation(Long id) {
        storage.containsOrElseThrow(id);
        return filmStorage.getFilmRecommendation(id);
    }

    public List<Feed> findAllUserFeed(Long id) {
        storage.containsOrElseThrow(id);
        return feedStorage.findAllUserFeed(id);
    }

    private void nameIsLoginIfNameIsNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
