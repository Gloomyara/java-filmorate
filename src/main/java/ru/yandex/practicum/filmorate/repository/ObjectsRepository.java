package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.util.Collection;
import java.util.Optional;

public interface ObjectsRepository<K, V> {

    Collection<V> findAll();

    Optional<V> getByKey(K k);

    V create(V v) throws ObjectAlreadyExistException;

    V put(V v) throws ObjectNotFoundException;
}
