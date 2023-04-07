package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.dao.UserRepositoryDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryDaoImpl implements UserRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select id, email, name, login, birthday from users";
        Collection<User> collection = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}: {}",
                "User", "User", collection.size()
        );
        return collection;
    }

    @Override
    public User getByKey(Integer k) throws ObjectNotFoundException {
        try {
            String sqlQuery = "select id, email, name, login, birthday " +
                    "from users where id = ?";
            User user = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, k))
                    .orElseThrow(
                            () -> new ObjectNotFoundException("User with Id: " + k + " not found")
                    );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "User", k
            );
            return user;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public User create(User user) throws ObjectAlreadyExistException {
        if (user.getId() != null) {
            int i = user.getId();
            log.warn(
                    "{} Id: {} should be null, Id генерируется автоматически.",
                    "User", i
            );
            throw new ObjectAlreadyExistException("User Id: " + i + " should be null," +
                    " Id генерируется автоматически.");
        }
        String sqlQuery = "insert into users(email, name, login, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User put(User v) throws ObjectNotFoundException {
        Integer k = v.getId();
        getByKey(k);
        String sqlQuery = "update employees set " +
                "email = ?, name = ?, login = ? , birthday = ? " +
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
        User v = getByKey(k1);
        getByKey(k2);
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                "select status from friends " +
                        "where user_id = ? " +
                        "and friend_id = ?", k2, k1);

        if (friendsRows.next()) {
            String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, k2, k1);
            String sqlQuery1 = "insert into friends(user_id, friend_id, status) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery1,
                    k1,
                    k2,
                    true);
            String sqlQuery2 = "insert into friends(user_id, friend_id, status) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery2,
                    k2,
                    k1,
                    true);
            log.debug(
                    "Подтверждение пользователем под Id:{} запроса на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнено!", k1, k2
            );
            return v;
        }
        String sqlQuery = "insert into friends(user_id, friend_id, status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                k1,
                k2,
                false);
        log.debug(
                "Запрос пользователя под Id: {} на добавление в друзья, " +
                        "пользователя под Id: {}, успешно выполнен!"
                , k1, k2
        );
        return v;
    }

    @Override
    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        try {
            User v = getByKey(k1);
            getByKey(k2);
            String sqlQuery1 = "delete from friends where user_id = ? and friend_id = ?";
            boolean b1 = jdbcTemplate.update(sqlQuery1, k1, k2) > 0;
            String sqlQuery2 = "delete from friends where user_id = ? and friend_id = ?";
            boolean b2 = jdbcTemplate.update(sqlQuery2, k2, k1) > 0;
            if (!b1) {
                throw new ObjectNotFoundException(
                        "Error! Cannot delete User Id: " + k1 + " friend with id: "
                                + k2 + ", user doesn't in your friends list!");
            }
            if (!b2) {
                throw new ObjectNotFoundException(
                        "Error! Cannot delete User Id: " + k2 + " friend with id: "
                                + k1 + ", user doesn't in your friends list!");
            }
            log.debug(
                    "Запрос пользователя под Id: {} на удаление из друзей, " +
                            "пользователя под Id: {}, успешно выполнен!",
                    k1, k2
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<User, Boolean> getFriendsListById(Integer k) throws ObjectNotFoundException {
        getByKey(k);
        String sqlQuery = "select u.id, u.email, u.name, u.login, u.birthday, f.status" +
                "from users u" +
                "join friends f on u.id=f.friend_id" +
                "where id in(select friend_id, status " +
                "from friends" +
                "where user_id = ?";
        Map<User, Boolean> tempMap = jdbcTemplate.query(sqlQuery, this::mapRowToMapEntry, k).stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен!\n" +
                        "Всего друзей в списке: {}.", k, tempMap.size()
        );
        return tempMap;
    }

    @Override
    public Collection<User> getMutualFriendsList(Integer k1, Integer k2) throws ObjectNotFoundException {
        getByKey(k1);
        getByKey(k2);
        String sqlQuery = "select id, email, name, login, birthday" +
                "from users" +
                "where id IN(SELECT friend_id" +
                "from friends" +
                "where user_id = ?" +
                "and confirmed = true" +
                "and friend_id in(select friend_id" +
                "from friends" +
                "where user_id = ?" +
                "and confirmed = true))";
        Collection<User> collection = jdbcTemplate.query(
                sqlQuery, this::mapRowToUser, k1, k2);
        log.debug(
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен!\n" +
                        "Всего общих друзей: {}.", k1, k2, collection.size()
        );
        return collection;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private Map.Entry<User, Boolean> mapRowToMapEntry(ResultSet resultSet, int rowNum) throws SQLException {
        return Map.entry(
                mapRowToUser(resultSet, rowNum)
                , resultSet.getBoolean("status")
        );
    }
}
