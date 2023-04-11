package ru.yandex.practicum.filmorate.service.inmemory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film.Rating;
import ru.yandex.practicum.filmorate.repository.film.RatingRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryRatingRepository;
import ru.yandex.practicum.filmorate.service.film.RatingService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class RatingServiceInMemoryTest {

    RatingRepository<Integer> ratingRepository;
    RatingService ratingService;
    Rating rating;

    @BeforeEach
    void createSomeData() {
        rating = new Rating(null, "rating1");
        ratingRepository = new InMemoryRatingRepository();
        ratingService = new RatingService(ratingRepository);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        assertTrue("Обнаружены неучтенные данные о рейтингах", ratingService.findAll().isEmpty());
    }

    @Test
    void findAll() {
        ratingService.create(rating);
        Rating rating2 = new Rating(rating.getId(), "rating1");
        Assertions.assertTrue(ratingService.findAll().contains(rating2), "Рейтинги не совпадают");
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 1;
        rating.setId(id);
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> ratingService.getByKey(id)
        );
        assertEquals("Rating with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        ratingService.create(rating);
        assertEquals(1, ratingService.findAll().size(), "Рейтинг не был добавлен в репозиторий");
        int id = rating.getId();
        Rating rating2 = new Rating(rating.getId(), "rating1");
        assertEquals(rating2, ratingService.getByKey(id), "Рейтинги не совпадают");
    }
}
