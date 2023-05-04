package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    void containsOrElseThrow(long id);

    T save(T t);

    T update(T t);

    Optional<T> findById(Long id);

    List<T> findAll();

    Optional<T> delete(Long id);
}
