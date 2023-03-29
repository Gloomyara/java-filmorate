package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController implements ObjectController<Film> {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    @Override
    public Collection<Film> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Override
    public Film getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        return service.create(film);
    }

    @PutMapping
    @Override
    public Film put(@Valid @RequestBody Film film) {
        return service.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {

        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {

        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count) {

        return service.getPopularFilms(count);
    }
}
