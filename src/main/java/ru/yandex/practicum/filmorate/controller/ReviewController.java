package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractController;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController extends AbstractController<Review, ReviewService> {


    protected ReviewController(ReviewService service) {
        super(service);
    }

    @PutMapping("{id}/like/{userId}")
    public Review addLike(@PathVariable @Positive long id,
                          @PathVariable @Positive long userId) {
        return service.addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review removeLike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return service.removeLikes(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review addDislike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return service.addDislike(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review removeDislike(@PathVariable @Positive long id,
                                @PathVariable @Positive long userId) {
        return service.removeDislikes(id, userId);
    }

    @GetMapping
    public List<Review> findAllWithParams(@RequestParam Map<String, String> requestParams) {
        long filmId = Long.parseLong(requestParams.getOrDefault("filmId", "0"));
        int count = Integer.parseInt(requestParams.getOrDefault("count", "10"));
        return service.findAllByFilmId(filmId, count);
    }
}
