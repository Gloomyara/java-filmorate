package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository
public class UserRepository extends ObjectsRepository<Integer, User> {
}
