package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController extends ObjectController<User> {
    @Autowired
    public UserController(UserService service) {
        super(service);
    }
}