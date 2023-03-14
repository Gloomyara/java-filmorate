package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ObjectService;

@RestController
@RequestMapping("/users")
public class UserController extends ObjectController<User> {

    public UserController(ObjectService<User> service) {
        super(service);
    }
}