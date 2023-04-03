package ru.yandex.practicum.filmorate.model.enums;

public enum FilmCategory {
    ACTION("Action", 0),
    ADVENTURE("Adventure", 1),
    ANIMATED("Animated", 2),
    COMEDY("Comedy", 3),
    DRAMA("Drama", 4),
    FANTASY("Fantasy", 5),
    HISTORICAL("Historical", 6),
    HORROR("Horror", 7),
    MUSICAL("Musical", 8),
    NOIR("Noir", 9),
    ROMANCE("Romance", 10),
    SCI_FI("Science fiction", 11),
    THRILLER("Thriller", 12),
    WESTERN("Western", 13);

    private final String categoryName;
    private final Integer categoryId;

    FilmCategory(String categoryName, Integer categoryId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public static FilmCategory fromString(String str) {
        for (FilmCategory cat : FilmCategory.values()) {
            if (cat.getCategoryName().equalsIgnoreCase(str)) return cat;
        }
        return null;
    }

    public static FilmCategory fromId(Integer id) {
        if (id < FilmCategory.values().length && id >= 0) {
            return FilmCategory.values()[id];
        }
        return null;
    }
}
