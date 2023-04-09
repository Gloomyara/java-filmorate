DROP
TABLE IF EXISTS
    FAVORITE_FILMS;

DROP
TABLE IF EXISTS
    FILM_GENRE;

DROP
TABLE IF EXISTS
    FILMS;

DROP
TABLE IF EXISTS
    FRIENDS;

DROP
TABLE IF EXISTS
    RATINGS;

DROP
TABLE IF EXISTS
    GENRES;

DROP
TABLE IF EXISTS
    USERS;

CREATE TABLE IF NOT EXISTS users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
login varchar(64) NOT NULL,
username varchar(64),
email varchar(128) NOT NULL,
birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
  user_id integer,
friend_user_id integer CONSTRAINT not_identiсal CHECK(friend_user_id != user_id),
status boolean DEFAULT FALSE,
CONSTRAINT uq_friends UNIQUE (user_id,
friend_user_id),
CONSTRAINT fk_fr_user_id
  FOREIGN KEY(user_id)
      REFERENCES users(id),
CONSTRAINT fk_fr_friend_user_id
      FOREIGN KEY(friend_user_id)
      REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS ratings (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(32),
description varchar(256)
/*CONSTRAINT uq_rating_name UNIQUE (name)*/
);

CREATE TABLE IF NOT EXISTS films (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
title varchar(128) NOT NULL,
description varchar(200),
LENGTH integer NOT NULL,
rating_id integer,
release_date date NOT NULL,
rate integer DEFAULT 0,
CONSTRAINT fk_f_rating_id
  FOREIGN KEY(rating_id) 
      REFERENCES ratings(id)
);

CREATE TABLE IF NOT EXISTS genres (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(64),
description varchar(1024)
/*CONSTRAINT uq_genre_name UNIQUE (name)*/
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id integer,
genre_id integer,
CONSTRAINT uq_film_genre UNIQUE (film_id,
genre_id),
CONSTRAINT fk_fc_genre_id
  FOREIGN KEY(genre_id) 
      REFERENCES genres(id),
CONSTRAINT fk_fc_film_id
      FOREIGN KEY(film_id) 
      REFERENCES films(id)
);

CREATE TABLE IF NOT EXISTS favorite_films (
  film_id integer,
user_id integer,
CONSTRAINT uq_favorite_films UNIQUE (film_id,
user_id),
CONSTRAINT fk_ff_user_id
  FOREIGN KEY(user_id) 
      REFERENCES users(id),
CONSTRAINT fk_ff_film_id
      FOREIGN KEY(film_id) 
      REFERENCES films(id)
);
