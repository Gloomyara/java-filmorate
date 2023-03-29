package ru.yandex.practicum.filmorate.service;

import java.util.Collection;


public interface ObjectService<T> {

    boolean repositoryContains(Integer id);

    Collection<T> findAll();

    T getById(Integer id);

    T create(T t);

    T put(T t);
}

