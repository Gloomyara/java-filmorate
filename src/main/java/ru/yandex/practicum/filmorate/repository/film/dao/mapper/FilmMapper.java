package ru.yandex.practicum.filmorate.repository.film.dao.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmMapper implements EntityMapper<Film> {

    private static final String TABLE_NAME = "film";
    private static final List<String> TABLE_FIELDS = List.of(
            "name",
            "description",
            "release",
            "duration");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Film film) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), film.getName());
        params.put(TABLE_FIELDS.get(1), film.getDescription());
        params.put(TABLE_FIELDS.get(2), film.getReleaseDate());
        params.put(TABLE_FIELDS.get(3), film.getDuration());
        return params;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Film film;
        Optional<MPARating> optV = Optional.empty();
        if (rs.getLong("mpa_id") > 0) {
            optV = Optional.of(
                    MPARating.builder()
                            .id(rs.getLong("mpa_id"))
                            .name(rs.getString("mpa_name"))
                            .build()
            );
        }
        film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString(TABLE_FIELDS.get(0)))
                .description(rs.getString(TABLE_FIELDS.get(1)))
                .releaseDate(rs.getDate(TABLE_FIELDS.get(2)).toLocalDate())
                .duration(rs.getInt(TABLE_FIELDS.get(3)))
                .rate(rs.getFloat("rate"))
                .mpa(optV.orElse(null))
                .genres(new ArrayList<>())
                .directors(new ArrayList<>())
                .build();
        if (rs.getString("genre_id_list") != null) {
            String[] genreId = rs.getString("genre_id_list").split(",");
            String[] genreName = rs.getString("genre_name_list").split(",");
            for (int i = 0; i < genreId.length; i++) {
                film.getGenres().add(Genre.builder()
                        .id(Long.parseLong(genreId[i]))
                        .name(genreName[i])
                        .build());
            }
        }
        if (rs.getString("director_id_list") != null) {
            String[] directorId = rs.getString("director_id_list").split(",");
            String[] directorName = rs.getString("director_name_list").split(",");
            for (int i = 0; i < directorId.length; i++) {
                film.getDirectors().add(Director.builder()
                        .id(Long.parseLong(directorId[i]))
                        .name(directorName[i])
                        .build());
            }
        }
        return film;
    }
}
