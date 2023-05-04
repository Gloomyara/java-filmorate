package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class FilmLike implements Entity {

    @NotNull
    private Long filmId;
    @NotNull
    private Long userId;
    private Integer rate;
    private Boolean isPositive;
}
