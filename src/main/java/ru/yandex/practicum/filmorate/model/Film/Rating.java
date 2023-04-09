package ru.yandex.practicum.filmorate.model.Film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Rating {
    private Integer id;
    /*@NotBlank(message = "Name cannot be blank")*/
    private String name;
}
