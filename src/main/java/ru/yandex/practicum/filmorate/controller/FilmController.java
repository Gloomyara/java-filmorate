package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/films")
public class FilmController extends ObjectController<Film> {
    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return service.create(film);
    }

    @PutMapping
    @Override
    public Film put(@Valid @RequestBody Film film) {
        return service.put(film);
    }
}