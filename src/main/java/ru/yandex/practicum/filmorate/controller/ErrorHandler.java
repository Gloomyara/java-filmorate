package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ObjectAlreadyExistException.class)
    public ResponseEntity<Object> handleObjectExistException(
            ObjectAlreadyExistException e, WebRequest request) {

        return new ResponseEntity<>(
                responseBody(HttpStatus.CONFLICT, e.getMessage(), request),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException e, WebRequest request) {

        return new ResponseEntity<>(
                responseBody(HttpStatus.NOT_FOUND, e.getMessage(), request),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException e, WebRequest request) {

        return new ResponseEntity<>(
                responseBody(HttpStatus.BAD_REQUEST, e.getMessage(), request),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(
                responseBody(status, ex.getMessage(), request),
                status);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleThrowable(
            final Throwable e, WebRequest request) {

        return new ResponseEntity<>(
                responseBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Map<String, Object> responseBody(HttpStatus status, String error, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", error);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}
