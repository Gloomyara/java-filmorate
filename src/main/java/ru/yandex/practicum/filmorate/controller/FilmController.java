package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ObjectService;


@RestController
@RequestMapping("/films")
public class FilmController extends ObjectController<Film> {

    public FilmController(ObjectService<Film> service) {
        super(service);
    }
}