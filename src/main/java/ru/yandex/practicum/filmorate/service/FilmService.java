package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends ObjectService<Integer, Film> {

    private final UserRepository<Integer> userRepository;
    private Integer id = 1;

    public FilmService(FilmRepository<Integer> repository, UserRepository<Integer> userRepository) {
        super(repository, "Film");
        this.userRepository = userRepository;
    }

    public boolean userRepositoryContainsKey(Integer k) {
        return userRepository.getByKey(k) != null;
    }

    @Override
    protected Integer getKey(Film film) {
        return film.getId();
    }

    @Override
    protected Integer objectPreparation(Film film) {
        film.setId(id);
        return id++;
    }

    public Film addLike(Integer id1, Integer id2) {

        Film f = getByKey(id1);
        if (!userRepositoryContainsKey(id2)) {
            log.warn(
                    "User Id: {} doesn't exist",
                    id2
            );
            throw new ObjectNotFoundException("User Id:" + id2 + " doesn't exist");
        }
        f.addLike(id2);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                id1, id2, f.getLikesInfo().size()
        );
        return f;
    }

    public Film deleteLike(Integer id1, Integer id2) {

        Film f = getByKey(id1);
        if (!userRepositoryContainsKey(id2) || !f.deleteLike(id2)) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    id2
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + id2 + " like, user like not found.");
        }

        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                id1, id2, f.getLikesInfo().size()
        );
        return f;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return repository.findAll().stream()
                .sorted((f0, f1) -> f1.getLikesInfo().size() - f0.getLikesInfo().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
