package ru.yandex.practicum.filmorate.controller.film;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.ObjectController;
import ru.yandex.practicum.filmorate.model.Film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController extends ObjectController<FilmService, Integer, Film> {

    public FilmController(FilmService service) {
        super(service);
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
