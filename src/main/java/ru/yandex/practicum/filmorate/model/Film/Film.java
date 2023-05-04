package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.film.customconstraint.FilmReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film implements Entity {

    private Long id;
    @NotBlank
    @JsonProperty("name")
    private String name;
    @Size(max = 200)
    @JsonProperty("description")
    private String description;
    @NotNull
    @FilmReleaseDateValidation(message = "ReleaseDate invalid", value = "1895-12-28")
    @JsonProperty("releaseDate")
    private LocalDate releaseDate;
    @Positive
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("rate")
    private float rate;
    @JsonProperty("genres")
    private List<Genre> genres;
    @JsonProperty("mpa")
    private MPARating mpa;
    @JsonProperty("directors")
    private List<Director> directors;

}