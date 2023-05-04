package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.MethodNotAllowedException;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.service.MPAService;
import ru.yandex.practicum.filmorate.service.Service;

import java.util.HashSet;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/mpa")
public class MPAController extends AbstractControllerWOParams<MPARating, MPAService> {

    private final Set<HttpMethod> notAllowedMethods = new HashSet<>();

    public MPAController(MPAService service) {
        super(service);
        notAllowedMethods.add(HttpMethod.POST);
        notAllowedMethods.add(HttpMethod.PUT);
        notAllowedMethods.add(HttpMethod.DELETE);
    }

    @Override
    public MPARating add(MPARating mpaRating) {
        throw new MethodNotAllowedException("POST", notAllowedMethods);
    }

    @Override
    public MPARating update(MPARating mpaRating) {
        throw new MethodNotAllowedException("PUT", notAllowedMethods);
    }

    @Override
    public void delete(Long id) {
        throw new MethodNotAllowedException("DELETE", notAllowedMethods);
    }
}
