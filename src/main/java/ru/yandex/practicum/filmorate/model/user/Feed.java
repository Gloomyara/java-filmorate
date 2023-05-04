package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;


@Data
@Builder
public class Feed implements Entity {

    @JsonProperty("eventId")
    private Long id;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("eventType")
    private EventType eventType;
    @JsonProperty("operation")
    private OperationType operation;
    @JsonProperty("entityId")
    private Long entityId;

}
