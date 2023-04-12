package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.dao.UserRepositoryDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository("UserRepositoryDao")
@Primary
public class UserRepositoryDaoImpl implements UserRepositoryDao<Integer> {
    private final JdbcTemplate jdbcTemplate;

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
    public Optional<User> getByKey(Integer k) {
        try {
            String sqlQuery = "select id, email, username, login, birthday " +
                    "from users where id = ?";
            Optional<User> optV = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, k));
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    "User", k
            );
            return optV;
        } catch (EmptyResultDataAccessException e) {
            log.warn("User with Id: {} not found", k);
            return Optional.empty();
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
        if (v.getName() == null || v.getName().isBlank()) {
            v.setName(v.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Integer k = simpleJdbcInsert.executeAndReturnKey(v.toMap()).intValue();
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
        String sqlQuery = "update users set " +
                "email = ?, username = ?, login = ?, birthday = ? " +
                "where id = ?";
        boolean b = jdbcTemplate.update(sqlQuery,
                v.getEmail(),
                v.getName(),
                v.getLogin(),
                Date.valueOf(v.getBirthday()),
                k) > 0;
        if (b) {
            log.debug(
                    "Данные {} по Id: {}, успешно обновлены.",
                    "Rating", k
            );
            return v;
        }
        log.warn("{} with Id: {} not found",
                "Rating", k);
        throw new ObjectNotFoundException("Rating with Id: " + k + " not found");
    }

    @Override
    public User addFriend(Integer k1, Integer k2) throws ObjectNotFoundException, ObjectAlreadyExistException {
        User v = getByKey(k1).orElseThrow(() -> new ObjectNotFoundException("User with Id: " + k1 + " not found"));
        try {
            String sqlQuery = "insert into friends(user_id, friend_user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    k1, k2);
            log.debug(
                    "Запрос пользователя под Id: {} на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнен!", k1, k2
            );
            return v;
        } catch (DuplicateKeyException e) {
            try {
                String sqlQuery1 = "insert into friends(user_id, friend_user_id, status) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery1,
                        k1, k2, true);
            } catch (DuplicateKeyException e1) {
                log.warn("Error! Cannot add in friends User with Id: " +
                        "{}, user already in your friends list.", k1);
                throw new ObjectAlreadyExistException("Error! Cannot add in friends User with Id: " +
                        k1 + ", user already in your friends list.");
            }
            String sqlQuery2 = "update friends set status =? " +
                    "where user_id = ? and friend_user_id = ?";
            jdbcTemplate.update(sqlQuery2,
                    true, k2, k1);
            log.debug(
                    "Подтверждение пользователем под Id:{} запроса на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнено!", k1, k2
            );
            return v;
        } catch (DataIntegrityViolationException e2) {
            log.warn("{} with Id: {} not found",
                    "User", k2);
            throw new ObjectNotFoundException("User with Id: " + k2 + " not found");
        }
    }

    @Override
    public User deleteFriend(Integer k1, Integer k2) throws ObjectNotFoundException {
        User v = getByKey(k1).orElseThrow(() -> new ObjectNotFoundException("User with Id: " + k1 + " not found"));
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
        return v;
    }

    @Override
    public Collection<User> getFriendsListById(Integer k) {

        String sqlQuery = "select id, email, username, login, birthday " +
                "from users u " +
                "where id in(select friend_user_id " +
                "from friends " +
                "where user_id = ? ) order by id";
        Collection<User> collection = jdbcTemplate.query(sqlQuery, this::mapRowToUser, k);
        log.debug(
                "Запрос списка друзей пользователя под Id: {} успешно выполнен! " +
                        "Всего друзей в списке: {}.", k, collection.size()
        );
        return collection;
    }

    @Override
    public Collection<User> getMutualFriendsList(Integer k1, Integer k2) {

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
                "Запрос списка общих друзей пользователей под Id: {} и Id: {} успешно выполнен! " +
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
}
