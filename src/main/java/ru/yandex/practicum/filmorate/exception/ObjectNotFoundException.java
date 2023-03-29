package ru.yandex.practicum.filmorate.exception;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException {
    public ObjectNotFoundException(String s) {
        super(s);
    }
}

