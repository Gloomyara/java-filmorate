package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;

public interface ObjectsRepository<K, V> {

    Collection<V> findAll();

    V getByKey(K k);

    void put(K k, V v);
}
