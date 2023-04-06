package ru.yandex.practicum.filmorate.repository.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.dao.UserRepositoryDao;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryDaoImpl implements UserRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public User getByKey(Integer integer) {
        /*
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from cat_user where id = ?", id);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getString("id"),
                    userRows.getString("username"),
                    userRows.getString("nickname"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getNickname());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }*/
        return null;
    }

    @Override
    public User create(User user) throws ObjectAlreadyExistException {
        return null;
    }

    @Override
    public User put(User user) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Map<User, Boolean> getFriendsListById(Integer k) throws ObjectNotFoundException {
        return null;
    }

    @Override
    public Collection<User> getMutualFriendsList(Integer k1, Integer k2) throws ObjectNotFoundException {
        return null;
    }
}
