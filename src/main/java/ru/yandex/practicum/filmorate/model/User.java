package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    @NotNull(message = "Email cannot be null")//иначе создается пользователь с email=null
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Login cannot be blank")
    private String login;
    private String name;
    //Без @NotNull создается пользователь с birthday=null.
    //В фильме дата NullPointerException выдает т.к. ее проверяю через иф.
    //Собственную аннотацию пытался сделать, но что-то пошло не так и в итоге она браковала все.
    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;
}
