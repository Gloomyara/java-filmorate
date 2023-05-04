package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Director implements Entity {

    private Long id;
    @NotBlank
    @JsonProperty("name")
    private String name;

}