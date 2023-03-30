package ru.yandex.practicum.filmorate.service;

import java.util.Collection;


public interface ObjectService<T> {

    boolean repositoryContainsKey(Integer id);

    Collection<T> findAll();

    T getByKey(Integer id);

    T create(T t);

    T put(T t);
}
