package ru.yandex.practicum.filmorate.repository.film.dao.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.repository.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DirectorMapper implements EntityMapper<Director> {

    @Override
    public String getTableName() {
        return "DIRECTOR";
    }

    @Override
    public List<String> getTableFields() {
        return List.of("name");
    }

    @Override
    public Map<String, Object> toMap(Director director) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(getTableFields().get(0), director.getName());
        return params;
    }

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Director.builder()
                .id(rs.getLong("id"))
                .name(rs.getString(getTableFields().get(0)))
                .build();
    }
}
