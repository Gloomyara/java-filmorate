package ru.yandex.practicum.filmorate.controller.abstractions;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.service.Service;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public abstract class AbstractController<T extends Entity, S extends Service<T>> implements Controller<T> {

    protected final S service;

    protected AbstractController(S service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public T get(@PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @PostMapping
    public T add(@Valid @RequestBody T t) {
        return service.create(t);
    }

    @PutMapping
    public T update(@Valid @RequestBody T t) {
        return service.update(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
    }
}
