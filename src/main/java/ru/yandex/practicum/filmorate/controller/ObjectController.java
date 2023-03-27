package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

public interface ObjectController<T> {

    Collection<T> findAll();

    T getById(Integer id);

    T create(T t);

    T put(T t);
}
