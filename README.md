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
   
<summary> DB Diagram </summary>

### [dbdiagram.io](https://dbdiagram.io/d/)

![Схема базы данных:](https://user-images.githubusercontent.com/115705343/230780900-0ff014a3-3ece-4950-846f-5ad7439ee51f.jpg)
   
</details>

   ### Короткое описание БД:
   
<details>

<summary> Filmorate DB description </summary>
   
   - База данных состоит из таблиц с данными о пользователях(*users*), друзьях(*friends*), любимых фильмах(*favorite_films*), фильмах(*films*), жанров фильмов(*category*) и служебной таблицы для связи фильмов и жанров(*film_category*).
   
   - Таблица _friends_ связана многие к одному с _PK(id)_ _users_, также имеет поле для определения связи(дружбы) между пользователями.
   
   - Таблицы _users_ и _films_ связаны один к многим по _PK(id)_ с таблицей *favorite_films* для хранения информации о любимых фильмах пользователя.
   
   - Таблицы _films_ и _category_ связаны один ко многим по _PK(id)_ с таблицей *film_category* для сортировки/поиска фильмов по жанрам, 
   а также для удовлетворения требований по нормализации баз данных.
   
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
6.             AND status = true
7.             AND user_friend_id IN(SELECT user_friend_id
8.                             FROM friends
9.                             WHERE user_id = Y
10.                            AND status = true))
```
   
</details>

   ### Получить список всех фильмов:
   
<details>

<summary> findAllFilms </summary> 

```sql 
1. SELECT *
2. FROM films f
```
   
</details>

   ### Получить список всех пользователей:
   
<details>

<summary> findAllUsers </summary> 
   
```sql
1. SELECT *
2. FROM users u
```
   
</details>

   ### Получить список N популярных фильмов:
   
<details>
   
<summary> topNMostPopularFilms </summary>
   
```sql
1.  SELECT *
2.  FROM films f
3.  ORDER BY rate DESC
4.  LIMIT N;              
```
   
</details>
</details>
</details>
