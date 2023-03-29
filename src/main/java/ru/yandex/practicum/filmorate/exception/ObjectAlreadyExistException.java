package ru.yandex.practicum.filmorate.exception;

public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException(String s) {
        super(s);
    }
}

