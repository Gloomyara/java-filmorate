package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.AbstractRepository;
import ru.yandex.practicum.filmorate.repository.user.dao.UserDao;
import ru.yandex.practicum.filmorate.repository.user.dao.mapper.UserMapper;

import java.util.List;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDaoImpl extends AbstractRepository<User> implements UserDao {

    private final FriendsDaoImpl friendsDaoImpl;

    public UserDaoImpl(JdbcTemplate jdbcTemplate,
                       FriendsDaoImpl friendsDaoImpl) {
        super(jdbcTemplate, new UserMapper());
        this.friendsDaoImpl = friendsDaoImpl;
    }

    @Override
    public User addFriend(Long id, Long friendId) {

        friendsDaoImpl.addFriend(id, friendId);
        return findById(id).orElseThrow();
    }

    @Override
    public User deleteFriend(Long id, Long friendId) throws EntityNotFoundException {

        friendsDaoImpl.deleteFriend(id, friendId);
        return findById(id).orElseThrow();
    }

    @Override
    public List<User> getFriendsListById(Long id) {

        String sqlQuery = "select id, " +
                getFieldsSeparatedByCommas() +
                " from " + mapper.getTableName() +
                " where id in(select user_friend_id" +
                " from user_friend" +
                " where user_id = ?) order by id";
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!", id
        );
        return jdbcTemplate.query(sqlQuery, mapper, id);
    }

    @Override
    public List<User> getMutualFriendsList(Long id, Long otherId) {

        String sqlQuery = "select id, " +
                getFieldsSeparatedByCommas() +
                " from " + mapper.getTableName() +
                "where id in(select user_friend_id " +
                "from user_friend " +
                "where user_id = ? " +
                "and user_friend_id in(select user_friend_id " +
                "from user_friend " +
                "where user_id = ?))" +
                "order by id";

        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!", id, otherId
        );
        return jdbcTemplate.query(sqlQuery, mapper, id, otherId);
    }
}