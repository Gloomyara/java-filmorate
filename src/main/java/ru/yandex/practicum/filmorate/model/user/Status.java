package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;

@Data
@Builder
public class Status implements Entity {

    private Long id;
    private String name;

}
