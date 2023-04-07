package ru.yandex.practicum.filmorate.repository.user.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface UserRepositoryDao<K> extends UserRepository<K> {

}
