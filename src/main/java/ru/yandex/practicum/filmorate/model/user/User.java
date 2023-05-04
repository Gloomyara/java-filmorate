package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User implements Entity {

    private Long id;
    @Email
    @NotNull
    @JsonProperty("email")
    private String email;
    @Pattern(regexp = "^\\S*$")
    @NotBlank
    @JsonProperty("login")
    private String login;
    @JsonProperty("name")
    private String name;
    @Past
    @NotNull
    @JsonProperty("birthday")
    private LocalDate birthday;
    @JsonProperty("friends")
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
