package ru.yandex.practicum.filmorate.repository.film.dao.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenreMapper implements EntityMapper<Genre> {

    private static final String TABLE_NAME = "genre";
    private static final List<String> TABLE_FIELDS = List.of("name");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Genre genre) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), genre.getName());
        return params;
    }

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString(TABLE_FIELDS.get(0)))
                .build();
    }
}
