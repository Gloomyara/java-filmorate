package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends ObjectController<Film> {

    public FilmController(FilmService service) {
        super(service);
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film ReleaseDate isBefore 28-12-1895");
            throw new ValidationException("Film ReleaseDate isBefore 28-12-1895");
        }
        return service.create(film);
    }

    @PutMapping
    @Override
    public Film put(@Valid @RequestBody Film film) {
        return service.put(film);
    }
}