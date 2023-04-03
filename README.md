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
   
   
<summary> Макет БД </summary>

   
![Схема базы данных:](https://user-images.githubusercontent.com/115705343/229374148-37baa50a-78f5-4478-8a79-220554435bba.png)
</details>
   
   ### Примеры SQL запросов из ТЗ:
   
<details>

<summary> SQL requests example </summary>

   ### Поиск общих друзей:
   
<details>

<summary> getMutualFriends </summary>
   
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

<summary> findAllFilms </summary> 

```sql 
1. SELECT *
2. FROM films f
3. GROUP BY f.id;
```
   
</details>

   ### Получить список всех пользователей:
   
<details>

<summary> findAllUsers </summary> 
   
```sql
1. SELECT *
2. FROM users u
3. GROUP BY u.id;
```
   
</details>

   ### Получить список N популярных фильмов:
   
<details>
   
<summary> topNMostPopularFilms </summary>
   
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

// очень круто орудуешь возможностями гитхаба! всякие раскрывающиеся флажки это эффектно как минимум
// радует, что мысли у нас идут в одном направлении. Хотя оно и понятно - теория то одна на всех!)
