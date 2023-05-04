package ru.yandex.practicum.filmorate.repository.user.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.repository.user.dao.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.repository.user.dao.FeedDao;

import java.util.List;

@Slf4j
@Component
public class FeedDaoImpl implements FeedDao {

    private final JdbcTemplate jdbcTemplate;
    private final FeedMapper mapper;

    public FeedDaoImpl(JdbcTemplate jdbcTemplate, FeedMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public void saveUserFeed(Feed feed) {
        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(mapper.getTableName())
                .usingGeneratedKeyColumns("id")
                .execute(mapper.toMap(feed));
        log.info("Сохранен: {}", feed);
    }

    @Override
    public List<Feed> findAllUserFeed(Long id) {
        return jdbcTemplate.query("select *" +
                        " from " + mapper.getTableName() +
                        " where user_id = ?",
                mapper, id);
    }
}
