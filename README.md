# **java-filmorate**

   ### **Final 9**

Каркас Spring Boot приложения "Filmorate" для оценки фильмов

   ### **Final 10**

Добавлены методы позволяющие пользователям добавлять друг друга в друзья, получать список общих друзей и лайкать фильмы.

   ### **Staging task 11**
    
Создание схемы базы данных

![Схема базы данных:](https://user-images.githubusercontent.com/115705343/229373113-1b8f7a1e-a322-41d4-85e5-e73b6b835dba.png)

*SQL* requests example:

- *getMutualFriends*

      1.  SELECT *
      2.  FROM users u
      3.  WHERE id IN(SELECT friend_id
      4.             FROM friends
      5.             WHERE user_id = X
      6.             AND confirmed = true
      7.             AND friend_id IN(SELECT friend_id
      8.                             FROM friends
      9.                             WHERE user_id = Y
      10.                            AND confirmed = true))
      11. GROUP BY u.id;

- *findAllFilms*

      1. SELECT *
      2. FROM films f
      3. GROUP BY f.id;

- *findAllUsers*

      1. SELECT *
      2. FROM users u
      3. GROUP BY u.id;

- *topNMostPopularFilms*

      1.  SELECT *
      2.  FROM films f
      3.  WHERE f.id IN(SELECT most_popular.film_id
      4.                FROM(SELECT film_id,
      5.                            COUNT(user_id) likes_count
      6.                    FROM user_likes
      7.                    GROUP BY film_id
      8.                    ORDER BY likes_count DESC
      9.                    LIMIT N) as most_popular)
      10. GROUP BY f.id;
