package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController extends ObjectController<Film> {
    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }
}