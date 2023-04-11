package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;
import ru.yandex.practicum.filmorate.controller.ObjectController;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/genres")
public class GenreController extends ObjectController<GenreService, Integer, Genre> {
    private final Set<HttpMethod> notAllowedMethods = new HashSet<>();

    public GenreController(GenreService service) {
        super(service);
        notAllowedMethods.add(HttpMethod.POST);
        notAllowedMethods.add(HttpMethod.PUT);
    }

    @PostMapping
    @Override
    public Genre create(@Valid @RequestBody Genre v) {
        throw new MethodNotAllowedException("POST", notAllowedMethods);
    }

    @PutMapping
    @Override
    public Genre put(@Valid @RequestBody Genre v) {
        throw new MethodNotAllowedException("PUT", notAllowedMethods);
    }
}
