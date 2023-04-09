package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.dao.UserRepositoryDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository("UserRepositoryDao")
@Primary
public class UserRepositoryDaoImpl implements UserRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsOrElseThrow(Integer k) throws ObjectNotFoundException {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select id from users where id = ?", k);
        if (userRows.next()) {
            return true;
        }
        throw new ObjectNotFoundException("User with Id: " + k + " not found");
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select id, email, username, login, birthday from users";
        Collection<User> collection = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "User", "User", collection.size()
        );
        return collection;
    }

    @Override
    public User getByKey(Integer k) throws ObjectNotFoundException {
        try {
            String sqlQuery = "select id, email, username, login, birthday " +
                    "from users where id = ?";
            User v = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, k);
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "User", k
            );
            return v;
        } catch (EmptyResultDataAccessException e) {
            log.warn("User with Id: {} not found", k);
            throw new ObjectNotFoundException("User with Id: " + k + " not found");
        }
    }

    @Override
    public User create(User v) throws ObjectAlreadyExistException {
        if (v.getId() != null) {
            int i = v.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "User", i
            );
            throw new ObjectAlreadyExistException("User Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        String sqlQuery = "insert into users(email, username, login, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (v.getName() == null || v.getName().isBlank()) {
            v.setName(v.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, v.getEmail());
            stmt.setString(2, v.getName());
            stmt.setString(3, v.getLogin());
            stmt.setDate(4, Date.valueOf(v.getBirthday()));
            return stmt;
        }, keyHolder);
        Integer k = Objects.requireNonNull(keyHolder.getKey()).intValue();
        v.setId(k);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                "User", k
        );
        return v;
    }

    @Override
    public User put(User v) throws ObjectNotFoundException {
        Integer k = v.getId();
        containsOrElseThrow(k);
        String sqlQuery = "update users set " +
                "email = ?, username = ?, login = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , v.getEmail()
                , v.getName()
                , v.getLogin()
                , Date.valueOf(v.getBirthday())
                , k);
        return v;
    }

    @Override
    public User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException {

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                "select status from friends " +
                        "where user_id = ? " +
                        "and friend_user_id = ?", k2, k1);

        if (friendsRows.next()) {

            String sqlQuery1 = "insert into friends(user_id, friend_user_id, status) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery1,
                    k1, k2, true);
            String sqlQuery2 = "update friends set status =? " +
                    "where user_id = ? and friend_user_id = ?";
            jdbcTemplate.update(sqlQuery2,
                    true, k2, k1);
            log.debug(
                    "Подтверждение пользователем под Id:{} запроса на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнено!", k1, k2
            );
            return getByKey(k1);
        }
        String sqlQuery = "insert into friends(user_id, friend_user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                k1, k2);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя под Id: {}, успешно выполнен!"
                , k1, k2
        );
        return getByKey(k1);
    }

    @Override
    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {

        String sqlQuery1 = "delete from friends where user_id = ? and friend_user_id = ?";
        boolean b = jdbcTemplate.update(sqlQuery1, k1, k2) > 0;
        if (!b) {
            log.warn(
                    "Error! Cannot delete User Id: {} friend with id: {}," +
                            " user doesn't in your friends list!", k1, k2
            );
            throw new ObjectNotFoundException("Error! Cannot delete User Id: "
                    + k1 + " friend with id: "
                    + k2 + ", user doesn't in your friends list!");
        }
        String sqlQuery2 = "delete from friends where user_id = ? and friend_user_id = ?";
        jdbcTemplate.update(sqlQuery2, k2, k1);
        log.debug(
                "Запрос пользователя под Id: {} на удаление из друзей, " +
                        "пользователя под Id: {}, успешно выполнен!",
                k1, k2
        );
        return getByKey(k1);
    }

    @Override
    public Collection<User> getFriendsListById(Integer k) throws ObjectNotFoundException {

        String sqlQuery = "select id, email, username, login, birthday " +
                "from users u " +
                "where id in(select friend_user_id " +
                "from friends " +
                "where user_id = ? ) order by id";
        Collection<User> collection = jdbcTemplate.query(sqlQuery, this::mapRowToUser, k);
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k, collection.size()
        );
        return collection;
    }

    @Override
    public Collection<User> getMutualFriendsList(Integer k1, Integer k2) throws ObjectNotFoundException {

        String sqlQuery = "select id, email, username, login, birthday " +
                "from users " +
                "where id in(select friend_user_id " +
                "from friends " +
                "where user_id = ? " +
                "and friend_user_id in(select friend_user_id " +
                "from friends " +
                "where user_id = ?))" +
                "order by id";
        Collection<User> collection = jdbcTemplate.query(
                sqlQuery, this::mapRowToUser, k1, k2);
        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", k1, k2, collection.size()
        );
        return collection;
    }

    @Override
    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("username"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    /*@Override
    public Map.Entry<User, Boolean> mapRowToMapEntry(ResultSet resultSet, int rowNum) throws SQLException {
        return Map.entry(
                mapRowToUser(resultSet, rowNum)
                , resultSet.getBoolean("status")
        );
    }*/
}
