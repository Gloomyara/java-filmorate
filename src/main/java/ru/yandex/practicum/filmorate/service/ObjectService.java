package ru.yandex.practicum.filmorate.service;

import java.util.Collection;


public interface ObjectService<K, V> {

    boolean repositoryContainsKey(K k);

    Collection<V> findAll();

    V getByKey(K k);

    V create(V v);

    V put(V v);
}
