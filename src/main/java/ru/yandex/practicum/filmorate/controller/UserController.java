package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController implements ObjectController<User> {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return service.put(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer friendId) {

        return service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer id,
                             @PathVariable("friendId") Integer friendId) {

        return service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsListById(@PathVariable("id") Integer id) {

        return service.getFriendsListById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriendsList(
            @PathVariable("id") Integer id,
            @PathVariable("otherId") Integer otherId) {

        return service.getMutualFriendsList(id, otherId);
    }
}
