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
    RATING;

DELETE
FROM
    GENRE;

DELETE
FROM
    USERS;

INSERT
    INTO
    RATING(NAME,
    DESCRIPTION)
VALUES ('G',
'У фильма нет возрастных ограничений!');

INSERT
    INTO
    RATING(NAME,
    DESCRIPTION)
VALUES ('PG',
'Детям рекомендуется смотреть фильм с родителями!');

INSERT
    INTO
    RATING(NAME,
    DESCRIPTION)
VALUES ('PG-13',
'Детям до 13 лет просмотр не желателен!');

INSERT
    INTO
    RATING(NAME,
    DESCRIPTION)
VALUES ('R',
'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого!');

INSERT
    INTO
    RATING(NAME,
    DESCRIPTION)
VALUES ('NC-17',
'Лицам до 18 лет просмотр запрещён!');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Action film',
'Associated with particular types of spectacle (e.g., explosions, chases, combat).');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Adventure film',
'Implies a narrative that is defined by a journey (often including some form of pursuit)
 and is usually located within a fantasy or exoticized setting. Typically, though not always,
 such stories include the quest narrative. The predominant emphasis on violence and fighting
 in action films is the typical difference between the two genres.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Animated film',
'A film medium in which the films images are primarily created by computer or hand
 and the characters are voiced by actors. Animation can incorporate any genre and subgenre.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Comedy film',
'Defined by events that are primarily intended to make the audience laugh.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Drama',
'Focused on emotions and defined by conflict, often looking to reality rather than sensationalism.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Fantasy film',
'Films defined by situations that transcend natural laws and/or by settings inside a fictional universe,
 with narratives that are often inspired by or involve human myths.');

/*
INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Historical film',
'Films that either provide more-or-less accurate representations of historical accounts 
 or depict fictional narratives placed inside an accurate depiction of a historical setting.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Horror film',
'Films that seek to elicit fear or disgust in the audience for entertainment purposes.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Musical film',
'A genre in which songs performed by the characters are interwoven into the narrative,
 sometimes accompanied by dancing. The songs usually advance the plot or develop
 the films characters or may serve merely as breaks in the storyline,
 often as elaborate "production numbers".');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Noir film',
'A genre of stylish crime dramas particularly popular during the 1940s and 50s.
 They were often reflective of the American society and culture at the time.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Romance film',
'Characterized by a emphasis on passion, emotion, and the affectionate romantic involvement
 of the main characters, with romantic love or the search for it typically being the primary focus.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Science fiction film',
'Films are defined by a combination of imaginative speculation and a scientific or technological premise,
 making use of the changes and trajectory of technology and science.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Thriller film',
'Films that evoke excitement and suspense in the audience. The suspense element found
 in most films plots is particularly exploited by the filmmaker in this genre.
 Tension is created by delaying what the audience sees as inevitable, and is built
 through situations that are menacing or where escape seems impossible.');

INSERT
    INTO
    GENRE(NAME,
    DESCRIPTION)
VALUES ('Western',
'A genre in which films are set in the American West during the 19th century.');*/