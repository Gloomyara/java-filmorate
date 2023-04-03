package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class ObjectService<R extends ObjectsRepository<K, V>, K, V> {
    protected final R repository;
    protected String valueClassName;

    protected boolean repositoryContainsKey(K k) {
        return repository.getByKey(k) != null;
    }

    public Collection<V> findAll() {
        Collection<V> collection = repository.findAll();
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}: {}",
                valueClassName, valueClassName, collection.size()
        );
        return collection;
    }

    public V getByKey(K k) throws ObjectNotFoundException {
        try {
            V v = Optional.ofNullable(repository.getByKey(k)).orElseThrow(
                    () -> new ObjectNotFoundException(valueClassName + " with Id: " + k + " not found")
            );
            log.debug(
                    "Запрос {} по Id: {} успешно выполнен.",
                    valueClassName, k
            );
            return v;
        } catch (ObjectNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public V create(V v) throws ObjectAlreadyExistException {
        K k = getKey(v);
        if (repositoryContainsKey(k)) {
            log.warn(
                    "{} под Id: {}, уже зарегистрирован.",
                    valueClassName, k
            );
            throw new ObjectAlreadyExistException(valueClassName + " под Id: " +
                    k + " уже зарегистрирован.");
        }

        k = objectPreparation(v);
        log.debug(
                "{} под Id: {}, успешно зарегистрирован.",
                valueClassName, k
        );
        repository.put(k, v);

        return v;
    }

    public V put(V v) throws ObjectNotFoundException {
        K k = getKey(v);
        getByKey(k);
        repository.put(k, v);
        log.debug(
                "Данные {} под Id: {}, успешно обновлены.",
                valueClassName, k
        );
        return v;
    }

    protected abstract K getKey(V v);

    protected abstract K objectPreparation(V v);
}
