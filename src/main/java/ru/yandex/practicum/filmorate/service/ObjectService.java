package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;

public abstract class ObjectService<K, V> {

    protected final ObjectsRepository<K, V> repository;
    private final String className;

    protected ObjectService(ObjectsRepository<K, V> repository, String className) {
        this.repository = repository;
        this.className = className;
    }

    public Collection<V> findAll() {
        return repository.findAll();
    }

    public V getByKey(K k) {
        return repository.getByKey(k).orElseThrow(
                () -> new ObjectNotFoundException(className + " with Id: " + k + " not found")
        );
    }

    public V create(V v) {
        return repository.create(v);
    }

    public V put(V v) {
        return repository.put(v);
    }

    protected abstract ObjectsRepository<K, V> getRepository();
}
