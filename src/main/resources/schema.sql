CREATE TABLE users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  username varchar(64),
  login varchar(64) NOT NULL,
  email varchar(128) NOT NULL,
  birthday date NOT NULL
);

CREATE TABLE friends (
  user_id integer
  friend_user_id integer
  status boolean
  CONSTRAINT fk_fr_user_id
  FOREIGN KEY(user_id)
      REFERENCES users(id)
      CONSTRAINT fk_fr_friend_user_id
      FOREIGN KEY(friend_user_id)
      REFERENCES users(id)
);

CREATE TABLE films (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title varchar(128) NOT NULL,
  description varchar(1024),
  LENGTH integer NOT NULL,
  rating_id integer,
  releaseDate date NOT NULL
  CONSTRAINT fk_f_rating_id
  FOREIGN KEY(rating_id) 
      REFERENCES rating(id)
);

CREATE TABLE category (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(32) NOT NULL,
  description varchar(256)
);

CREATE TABLE film_category (
  category_id integer,
  film_id integer
  CONSTRAINT fk_fc_category_id
  FOREIGN KEY(category_id) 
      REFERENCES category(id)
      CONSTRAINT fk_fc_film_id
      FOREIGN KEY(film_id) 
      REFERENCES films(id)
);

CREATE TABLE rating (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(32) NOT NULL,
  description varchar(256)
);

CREATE TABLE favorite_films (
  user_id integer,
  film_id integer
  CONSTRAINT fk_ff_user_id
  FOREIGN KEY(user_id) 
      REFERENCES users(id)
      CONSTRAINT fk_ff_film_id
      FOREIGN KEY(film_id) 
      REFERENCES films(id)
);
