package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController extends ObjectController<UserService, User> {

    public UserController(UserService service) {
        super(service);
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
