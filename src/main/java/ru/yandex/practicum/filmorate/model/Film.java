package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.customconstraint.FilmReleaseDateValidation;
import ru.yandex.practicum.filmorate.model.enums.FilmCategory;
import ru.yandex.practicum.filmorate.model.enums.FilmRating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {

    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;
    @FilmReleaseDateValidation(message = "ReleaseDate invalid", value = "1895-12-28")
    private LocalDate releaseDate;
    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration should be positive")
    private Integer length;
    private FilmRating rating;
    private FilmCategory category;

    private final Set<Integer> likesInfo = new HashSet<>();

    public void addLike(Integer userId) {
        likesInfo.add(userId);
    }

    public boolean deleteLike(Integer userId) {
        return likesInfo.remove(userId);
    }
}
