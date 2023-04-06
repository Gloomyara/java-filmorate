package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface ObjectService<K, V> {

    Collection<V> findAll();

    V getByKey(K k);

    V create(V v);

    V put(V v);
}
