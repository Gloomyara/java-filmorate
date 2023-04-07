package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Integer id;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Login cannot be blank")
    private String login;
    private String username;
    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;
}
