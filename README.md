# **java-filmorate**

   ### **Final 9**

Каркас Spring Boot приложения "Filmorate" для оценки фильмов

   ### **Final 10**

Добавлены методы позволяющие пользователям добавлять друг друга в друзья, получать список общих друзей и лайкать фильмы.

   ### **Staging task 11**
   
<details>

<summary> Создание схемы базы данных </summary>
   
   ### Схема:
   
<details>
   
<summary> ${\color{lightgreen}DB \space Diagram}$ </summary>
   
   ### [dbdiagram.io](https://dbdiagram.io/d/)
   

![Схема базы данных:](https://user-images.githubusercontent.com/115705343/229625278-628874b4-f287-4528-b827-41d944ab3671.png)
</details>

   ### Короткое описание БД:
   
<details>

<summary> ${\color{lightgreen}Filmorate \space DB \space description}$ </summary>
   
   - База данных состоит из таблиц с данными о пользователях(${\color{lightblue}users}$), друзьях(${\color{lightblue}friends}$), любимых фильмах(${\color{lightblue}favorite \space films}$), фильмах(${\color{lightblue}films}$), жанров фильмов(${\color{lightblue}category}$) и служебной таблицы для связи фильмов и жанров(${\color{lightblue}film \space category}$).
   
   - Таблица ${\color{lightblue}friends}$ связана многие к одному с ${\color{pink}PK\color{lightgreen}(\color{purple}id\color{lightgreen})}$ ${\color{lightblue}users}$, также имеет поле для определения связи/дружбы между пользователями.
   
   - Таблицы ${\color{lightblue}users}$ и ${\color{lightblue}films}$ связаны один к многим по ${\color{pink}PK\color{lightgreen}(\color{purple}id\color{lightgreen})}$ с таблицей ${\color{lightblue}favorite \space films}$ для хранения информации о любимых фильмах пользователя.
   
   - Таблицы ${\color{lightblue}films}$ и ${\color{lightblue}category}$ связаны один ко многим по ${\color{pink}PK\color{lightgreen}(\color{purple}id\color{lightgreen})}$ с таблицей ${\color{lightblue}film \space category}$ для сортировки/поиска фильмов по жанрам, 
   а также для удовлетворения требований по нормализации баз данных.
   
</details>
   
   ### Примеры SQL запросов из ТЗ:
   
<details>

<summary> ${\color{lightgreen}SQL \space requests \space example}$ </summary>

   ### Поиск общих друзей:
   
<details>

<summary> ${\color{orange}getMutualFriends()}$ </summary>
   
```sql   
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
```
   
</details>

   ### Получить список всех фильмов:
   
<details>

<summary> ${\color{orange}findAllFilms()}$ </summary> 

```sql 
1. SELECT *
2. FROM films f
3. GROUP BY f.id;
```
   
</details>

   ### Получить список всех пользователей:
   
<details>

<summary> ${\color{orange}findAllUsers()}$ </summary> 
   
```sql
1. SELECT *
2. FROM users u
3. GROUP BY u.id;
```
   
</details>

   ### Получить список N популярных фильмов:
   
<details>
   
<summary> ${\color{orange}topNMostPopularFilms()}$ </summary>
   
```sql
1.  SELECT *
2.  FROM films f
3.  WHERE f.id IN (SELECT most_popular.film_id
4.                FROM (SELECT film_id,
5.                             COUNT(user_id) likes_count
6.                      FROM user_likes
7.                      GROUP BY film_id
8.                      ORDER BY likes_count DESC
9.                      LIMIT N) as most_popular)
10. GROUP BY f.id;
```
   
</details>
</details>
</details>
