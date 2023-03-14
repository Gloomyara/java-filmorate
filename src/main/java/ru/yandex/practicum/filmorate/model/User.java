package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull(message = "Id cannot be null")
    @Positive(message = "id should be positive")
    private Integer id;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Login cannot be blank")
    private String login;
    private String name;
    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;

    public User() {
        if (name.isBlank()) name = login;
    }

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }
}
