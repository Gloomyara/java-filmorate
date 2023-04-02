package ru.yandex.practicum.filmorate.model.enums;

public enum FilmRating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");
    private final String ratingName;

    FilmRating(String ratingName) {
        this.ratingName = ratingName;
    }

    public String getRatingName() {
        return ratingName;
    }

    public static FilmRating fromString(String str) {
        for (FilmRating r : FilmRating.values()) {
            if (r.getRatingName().equalsIgnoreCase(str)) return r;
        }
        return null;
    }
}
