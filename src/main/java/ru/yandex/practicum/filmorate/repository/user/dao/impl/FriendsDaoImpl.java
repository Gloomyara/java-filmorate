package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.repository.user.dao.FriendsDao;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                "select status_id from user_friend " +
                        "where user_id = ? " +
                        "and user_friend_id = ?", friendId, userId);

        if (friendsRows.next()) {

            String sqlQuery1 = "insert into user_friend(user_id, user_friend_id, status_id) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery1,
                    userId, friendId, 1);
            String sqlQuery2 = "update friends set status_id =? " +
                    "where user_id = ? and user_friend_id = ?";
            jdbcTemplate.update(sqlQuery2, 1, friendId, userId);
            log.debug(
                    "Подтверждение пользователем под Id:{} запроса на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнено!", userId, friendId
            );
        } else {
            String sqlQuery = "insert into friends(user_id, user_friend_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
            log.debug(
                    "Запрос пользователя под Id: {} на добавление в друзья, " +
                            "пользователя под Id: {}, успешно выполнен!", userId, friendId
            );
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) throws EntityNotFoundException {

        String sqlQuery1 = "delete from user_friend where user_id = ? and user_friend_id = ?";
        boolean b = jdbcTemplate.update(sqlQuery1, userId, friendId) > 0;
        if (!b) {
            log.warn(
                    "Error! Cannot delete User Id: {} friend with id: {}," +
                            " user doesn't in your friends list!", userId, friendId
            );
            throw new EntityNotFoundException("Error! Cannot delete User Id: "
                    + userId + " friend with id: "
                    + friendId + ", user doesn't in your friends list!");
        }
        String sqlQuery2 = "delete from user_friend where user_id = ? and user_friend_id = ?";
        jdbcTemplate.update(sqlQuery2, friendId, userId);
        log.debug(
                "Запрос пользователя под Id: {} на удаление из друзей, " +
                        "пользователя под Id: {}, успешно выполнен!",
                userId, friendId
        );
    }
}
