package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.util.Collection;

public interface ObjectsRepository<K, V> {

    Collection<V> findAll();

    V getByKey(K k);

    V create(V v) throws ObjectAlreadyExistException;

    V put(V v) throws ObjectNotFoundException;
}
