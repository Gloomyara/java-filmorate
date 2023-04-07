package ru.yandex.practicum.filmorate.model.Film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film.customconstraint.FilmReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String title;
    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;
    @FilmReleaseDateValidation(message = "ReleaseDate invalid", value = "1895-12-28")
    private LocalDate releaseDate;
    @NotNull(message = "Length cannot be null")
    @Positive(message = "Length should be positive")
    private Integer length;
    private Integer ratingId;
    private Set<Integer> genreIdSet;
    @NotNull(message = "Rate cannot be null")
    private Integer rate;
}
