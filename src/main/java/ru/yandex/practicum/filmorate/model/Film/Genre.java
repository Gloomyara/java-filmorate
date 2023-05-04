package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Entity;

@Data
@Builder
public class Genre implements Entity {

    private Long id;
    @JsonProperty("name")
    private String name;
}

