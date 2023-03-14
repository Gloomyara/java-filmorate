package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ObjectService;

import javax.validation.constraints.NotNull;
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

    @PostMapping
    public T create(@NotNull @RequestBody T t) {
        return service.create(t);
    }

    @PutMapping
    public T put(@NotNull @RequestBody T t) {
        return service.put(t);
    }
}
