package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;
import ru.yandex.practicum.filmorate.service.ObjectService;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
public abstract class ObjectController<S extends ObjectService<R, K, V>,
        K, V, R extends ObjectsRepository<K, V>> {

    protected final S service;

    @GetMapping
    public Collection<V> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public V getByKey(@PathVariable("id") K k) {
        return service.getByKey(k);
    }

    @PostMapping
    public V create(@Valid @RequestBody V v) {
        return service.create(v);
    }

    @PutMapping
    public V put(@Valid @RequestBody V v) {
        return service.put(v);
    }
}
