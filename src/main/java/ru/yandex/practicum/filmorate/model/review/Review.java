package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review implements Entity {

    @JsonProperty("reviewId")
    private Long id;
    @NotBlank
    @JsonProperty("content")
    private String content;
    @NotNull
    @JsonProperty("isPositive")
    private Boolean isPositive;
    @NotNull
    @JsonProperty("userId")
    private Long userId;
    @NotNull
    @JsonProperty("filmId")
    private Long filmId;
    @JsonProperty("useful")
    private Integer useful;

}
