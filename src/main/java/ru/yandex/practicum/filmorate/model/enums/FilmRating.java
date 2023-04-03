package ru.yandex.practicum.filmorate.model.enums;

public enum FilmRating {
    G("G", "У фильма нет возрастных ограничений"),
    PG("PG", "Детям рекомендуется смотреть фильм с родителями"),
    PG_13("PG-13", "Детям до 13 лет просмотр не желателен"),
    R("R", "Лицам до 17 лет просматривать фильм " +
            "можно только в присутствии взрослого"),
    NC_17("NC-17", " лицам до 18 лет просмотр запрещён");
    private final String ratingName;
    private final String ratingDescription;

    FilmRating(String ratingName, String ratingDescription) {
        this.ratingName = ratingName;
        this.ratingDescription = ratingDescription;
    }

    public String getRatingName() {
        return ratingName;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public static FilmRating fromString(String str) {
        for (FilmRating r : FilmRating.values()) {
            if (r.getRatingName().equalsIgnoreCase(str)) return r;
        }
        return null;
    }
}
