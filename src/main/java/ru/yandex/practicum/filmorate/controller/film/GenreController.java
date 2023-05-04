package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.MethodNotAllowedException;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.Service;

import java.util.HashSet;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/genres")
public class GenreController extends AbstractControllerWOParams<Genre, GenreService> {

    private final Set<HttpMethod> notAllowedMethods = new HashSet<>();

    public GenreController(GenreService service) {
        super(service);
        notAllowedMethods.add(HttpMethod.POST);
        notAllowedMethods.add(HttpMethod.PUT);
        notAllowedMethods.add(HttpMethod.DELETE);
    }

    @Override
    public Genre add(Genre genre) {
        throw new MethodNotAllowedException("POST", notAllowedMethods);
    }

    @Override
    public Genre update(Genre genre) {
        throw new MethodNotAllowedException("PUT", notAllowedMethods);
    }

    @Override
    public void delete(Long id) {
        throw new MethodNotAllowedException("DELETE", notAllowedMethods);
    }
}
