package ru.yandex.practicum.filmorate.service.inmemory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.repository.film.inmemory.InMemoryGenreRepository;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class GenreServiceInMemoryTest {
    GenreRepository<Integer> genreRepository;
    GenreService genreService;
    Genre genre;

    @BeforeEach
    void createSomeData() {
        genre = new Genre(null, "genre1");
        genreRepository = new InMemoryGenreRepository();
        genreService = new GenreService(genreRepository);
    }

    @Test
    void findAllShouldBeIsEmpty() {
        assertTrue("Обнаружены неучтенные данные о жанрах", genreService.findAll().isEmpty());
    }

    @Test
    void findAll() {
        genreService.create(genre);
        Genre genre2 = new Genre(genre.getId(), "genre1");
        Assertions.assertTrue(genreService.findAll().contains(genre2), "Жанры не совпадают");
    }

    @Test
    void getByIdShouldThrowNoSuchElementException() {
        int id = 1;
        genre.setId(id);
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> genreService.getByKey(id)
        );
        assertEquals("Genre with Id: " + id + " not found", ex.getMessage());
    }

    @Test
    void getById() {
        genreService.create(genre);
        assertEquals(1, genreService.findAll().size(), "Жанр не был добавлен в репозиторий");
        int id = genre.getId();
        Genre genre2 = new Genre(id, "genre1");
        assertEquals(genre2, genreService.getByKey(id), "Жанры не совпадают");
    }
}
