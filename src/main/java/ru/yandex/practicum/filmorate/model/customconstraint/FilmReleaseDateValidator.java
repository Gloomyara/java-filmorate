package ru.yandex.practicum.filmorate.model.customconstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDateValidation, LocalDate> {
    private LocalDate FirstFilmDate;

    @Override
    public void initialize(FilmReleaseDateValidation constraintAnnotation) {
        this.FirstFilmDate = LocalDate.parse(constraintAnnotation.value(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return false;
        }
        return object.isAfter(FirstFilmDate);
    }
}
