# java-filmorate

### Промежуточное задание месяца SQL

<details>

<summary>ER-диаграмма</summary>

![schema.png](docs/db_schema/schema.png)

</details>

<details>

<summary>Описание базы данных</summary>

## films

Содержит информацию о фильмах.
Таблица включает такие поля:
* первичный ключ **film_id** - идентификатор фильма;
* **title** - название фильма;
* **description** - описание фильма;
* **release_date** - дата релиза;
* **duration** - продолжительность фильма в минутах;
* **mpa_rating** - рейтинг Ассоциации кинокомпаний.

## film_genres

Содержит информацию о жанрах фильмов из таблицы films.
Таблица включает такие поля:
* составной первичный ключ **film_id** - идентификатор фильма;
* составной первичный ключ **genre_id** - идентификатор жанра.

## film_likes

Содержит информацию о лайках фильмов пользователями.
Таблица включает такие поля:
* составной первичный ключ **film_id** - идентификатор фильма;
* составной первичный ключ **user_id** - идентификатор пользователя.

## friendship

Содержит информацию о дружбе пользователей.
Таблица включает такие поля:
* составной первичный ключ **user_id** - идентификатор пользователя,
* составной первичный ключ **friend_id** - идентификатор друга,
* status - статус дружбы(подтвержденная или неподтвержденная).

## genres

Содержит информацию о жанрах фильмов.
Таблица включает такие поля:
* первичный ключ **genre_id** - идентификатор жанра
* **name** - название жанра.

## mpa_ratings

Содержит информацию о возрастных ограничениях фильмов.
Таблица включает такие поля:
* первичный ключ **mpa_rating** - код рейтинга Ассоциации кинокомпаний(G, PG, PG-13, R, NC-17),
* **description** - описание кода рейтинга.

## users

Содержит информацию о пользователях.
Таблица включает такие поля:
* первичный ключ **user_id** - идентификатор пользователя,
* **email** - электронная почта,
* **login** - логин,
* **name** - имя,
* **birthday** - дата рождения.

</details>

<details>

<summary>Примеры запросов</summary>

### Фильмы определенного жанра
```
SELECT f.title, g.name AS genre
FROM films f
JOIN film_genres fg ON f.film_id = fg.film_id
JOIN genres g fg.genre_id = g.genre_id
WHERE g.name = "Триллер";
```

### Самые популярные фильмы
```
SELECT f.title, COUNT(fl.user_id) AS likes
FROM films f
LEFT JOIN film_likes fl ON f.film_id = fl.film_id
GROUP BY f.title
ORDER BY likes DESC;
```

### Количество фильмов по MPA рейтингам
```
SELECT m.mpa_rating, m.description, COUNT(f.film_id) AS films_count
FROM mpa_ratings m
LEFT JOIN films f ON m.mpa_rating = f.mpa_rating
GROUP BY m.mpa_rating, m.description
ORDER BY films_count DESC;
```

### Фильмы, понравившиеся друзьям
```
SELECT DISTINCT f.title
FROM films f
JOIN film_likes fl ON f.film_id = fl.film_id
JOIN friendships fs ON fl.user_id = fs.friend_id
WHERE fs.user_id = 1
AND fs.status = 'CONFIRMED';
```

</details>