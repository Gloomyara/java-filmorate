package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ObjectService;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
public abstract class ObjectController<S extends ObjectService<T>, T> {

    protected final S service;

    @GetMapping
    public Collection<T> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public T getById(@PathVariable("id") Integer id) {
        return service.getById(id);
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

