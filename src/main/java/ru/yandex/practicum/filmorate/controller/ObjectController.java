package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.service.ObjectService;

import javax.validation.Valid;
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
    public T create(@Valid @RequestBody T t) {
        return service.create(t);
    }

    @PutMapping
    public T put(@Valid @RequestBody T t) {
        return service.put(t);
    }
}
