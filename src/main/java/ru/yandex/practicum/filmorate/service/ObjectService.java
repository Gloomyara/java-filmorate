package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public abstract class ObjectService<T> {
    protected Integer id = 1;

    public ObjectService() {
    }

    public abstract Collection<T> findAll();

    public abstract T create(T t);

    public abstract T put(T t);
}
