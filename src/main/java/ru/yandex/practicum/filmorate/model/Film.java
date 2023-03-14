package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    @NotNull(message = "Id cannot be null")
    @Positive(message = "Id should be positive")
    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;
    @NotNull(message = "ReleaseDate cannot be null")
    private LocalDate releaseDate;
    @NotNull(message = "durationInSeconds cannot be null")
    @Positive(message = "DurationInSeconds should be positive")
    private Integer durationInSeconds;
    private final Duration duration;
    @AssertTrue(message = "ReleaseDate should be is after 28.12.1895")
    private final boolean dateValidation;

    public Film() {
        duration = Duration.ofSeconds(durationInSeconds);
        dateValidation = releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }

    public Film(Integer id, String name, String description,
                LocalDate releaseDate, Integer durationInSeconds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.durationInSeconds = durationInSeconds;
        this.duration = Duration.ofSeconds(durationInSeconds);
        this.dateValidation = releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
