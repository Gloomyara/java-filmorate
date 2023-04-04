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
    protected Integer getKey(Film v) {
        return v.getId();
    }

    @Override
    protected Integer objectPreparation(Film film) {
        film.setId(id);
        return id++;
    }

    public Film addLike(Integer k1, Integer k2) {

        Film v = getByKey(k1);
        if (!userRepositoryContainsKey(k2)) {
            log.warn(
                    "User Id: {} doesn't exist",
                    k2
            );
            throw new ObjectNotFoundException("User Id:" + k2 + " doesn't exist");
        }
        v.addLike(k2);
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, v.getLikesInfo().size()
        );
        return v;
    }

    public Film deleteLike(Integer k1, Integer k2) {

        Film v = getByKey(k1);
        if (!userRepositoryContainsKey(k2) || !v.deleteLike(k2)) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    k2
            );
            throw new ObjectNotFoundException("Error! Cannot delete user Id: "
                    + k2 + " like, user like not found.");
        }

        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, v.getLikesInfo().size()
        );
        return v;
    }

    public Collection<Film> getPopularFilms(Integer i) {
        return repository.findAll().stream()
                .sorted((f0, f1) -> f1.getLikesInfo().size() - f0.getLikesInfo().size())
                .limit(i)
                .collect(Collectors.toList());
    }
}
