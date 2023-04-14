package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;

@Slf4j
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
