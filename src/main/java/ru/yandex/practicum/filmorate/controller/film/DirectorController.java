package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.Service;

@RestController
@Validated
@RequestMapping("/directors")
public class DirectorController extends AbstractControllerWOParams<Director, DirectorService> {

    public DirectorController(DirectorService service) {
        super(service);
    }

}