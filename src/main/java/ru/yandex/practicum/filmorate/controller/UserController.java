package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController extends ObjectController<User> {
    @Autowired
    public UserController(UserService service) {
        super(service);
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PutMapping
    @Override
    public User put(@Valid @RequestBody User user) {
        return service.put(user);
    }
}