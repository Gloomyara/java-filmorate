package ru.yandex.practicum.filmorate.repository.film.dao.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MPAMapper implements EntityMapper<MPARating> {

    private static final String TABLE_NAME = "mpa_rating";
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
    public Map<String, Object> toMap(MPARating mpa) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), mpa.getName());
        return params;
    }

    @Override
    public MPARating mapRow(ResultSet rs, int rowNum) throws SQLException {

        return MPARating.builder()
                .id(rs.getLong("id"))
                .name(rs.getString(TABLE_FIELDS.get(0)))
                .build();
    }
}
