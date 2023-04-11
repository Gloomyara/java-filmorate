package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;
import ru.yandex.practicum.filmorate.controller.ObjectController;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.service.film.RatingService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/mpa")
public class RatingController extends ObjectController<RatingService, Integer, Rating> {
    private final Set<HttpMethod> notAllowedMethods = new HashSet<>();

    public RatingController(RatingService service) {
        super(service);
        notAllowedMethods.add(HttpMethod.POST);
        notAllowedMethods.add(HttpMethod.PUT);
    }

    @PostMapping
    @Override
    public Rating create(@Valid @RequestBody Rating v) {
        throw new MethodNotAllowedException("POST", notAllowedMethods);
    }

    @PutMapping
    @Override
    public Rating put(@Valid @RequestBody Rating v) {
        throw new MethodNotAllowedException("PUT", notAllowedMethods);
    }
}
