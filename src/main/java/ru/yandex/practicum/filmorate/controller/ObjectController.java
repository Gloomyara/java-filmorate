package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.service.ObjectService;

import java.util.Collection;

public abstract class ObjectController<T> {
    protected final ObjectService<T> service;

    public ObjectController(ObjectService<T> service) {
        this.service = service;
    }

    @GetMapping
    public Collection<T> findAll() {
        return service.findAll();
    }

    public abstract T create(T t);

    public abstract T put(T t);
}
