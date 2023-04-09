DELETE
FROM
    FAVORITE_FILMS;

DELETE
FROM
    FILM_GENRE;

DELETE
FROM
    FILMS;

DELETE
FROM
    FRIENDS;

DELETE
FROM
    RATINGS;

DELETE
FROM
    GENRES;

DELETE
FROM
    USERS;

INSERT
    INTO
    RATINGS(NAME,
    DESCRIPTION)
VALUES ('G',
'У фильма нет возрастных ограничений!');

INSERT
    INTO
    RATINGS(NAME,
    DESCRIPTION)
VALUES ('PG',
'Детям рекомендуется смотреть фильм с родителями!');

INSERT
    INTO
    RATINGS(NAME,
    DESCRIPTION)
VALUES ('PG-13',
'Детям до 13 лет просмотр не желателен!');

INSERT
    INTO
    RATINGS(NAME,
    DESCRIPTION)
VALUES ('R',
'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого!');

INSERT
    INTO
    RATINGS(NAME,
    DESCRIPTION)
VALUES ('NC-17',
'Лицам до 18 лет просмотр запрещён!');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Комедия',
'Defined by events that are primarily intended to make the audience laugh.');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Драма',
'Focused on emotions and defined by conflict, often looking to reality rather than sensationalism.');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Мультфильм',
'A film medium in which the films images are primarily created by computer or hand
 and the characters are voiced by actors. Animation can incorporate any GENRES and subGENRES.');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Триллер',
'Films that evoke excitement and suspense in the audience. The suspense element found
 in most films plots is particularly exploited by the filmmaker in this GENRES.
 Tension is created by delaying what the audience sees as inevitable, and is built
 through situations that are menacing or where escape seems impossible.');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Документальный',
'Films that either provide more-or-less accurate representations of historical accounts 
 or depict fictional narratives placed inside an accurate depiction of a historical setting.');

INSERT
    INTO
    GENRES(NAME,
    DESCRIPTION)
VALUES ('Боевик',
'Associated with particular types of spectacle (e.g., explosions, chases, combat).');
