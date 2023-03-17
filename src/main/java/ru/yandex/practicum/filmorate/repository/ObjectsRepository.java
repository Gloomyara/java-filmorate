package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.Map;

public interface ObjectsRepository<K, V> {

    Collection<V> findAll();

    Map<K, V> get();
}
