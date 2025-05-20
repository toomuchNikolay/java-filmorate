-- Заполнение таблицы mpa_ratings
INSERT INTO mpa_ratings (name) SELECT 'G' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'G');
INSERT INTO mpa_ratings (name) SELECT 'PG' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'PG');
INSERT INTO mpa_ratings (name) SELECT 'PG-13' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'PG-13');
INSERT INTO mpa_ratings (name) SELECT 'R' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'R');
INSERT INTO mpa_ratings (name) SELECT 'NC-17' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'NC-17');

--Заполнение таблицы genres
INSERT INTO genres (name) SELECT 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия');
INSERT INTO genres (name) SELECT 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма');
INSERT INTO genres (name) SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм');
INSERT INTO genres (name) SELECT 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер');
INSERT INTO genres (name) SELECT 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный');
INSERT INTO genres (name) SELECT 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');

--Заполнение таблицы users
INSERT INTO users (email, login, name, birthday)
SELECT 'user1@mail.com', 'user111', 'user1', '2000-01-01'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user1@mail.com');

INSERT INTO users (email, login, name, birthday)
SELECT 'user2@mail.com', 'user222', 'user2', '2000-02-02'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user2@mail.com');

INSERT INTO users (email, login, name, birthday)
SELECT 'user3@mail.com', 'user333', 'user3', '2000-03-03'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user3@mail.com');

INSERT INTO users (email, login, name, birthday)
SELECT 'user4@mail.com', 'user444', 'user4', '2000-04-04'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user4@mail.com');

INSERT INTO users (email, login, name, birthday)
SELECT 'user5@mail.com', 'user555', 'user5', '2000-05-05'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user5@mail.com');

--Заполнение таблицы films
INSERT INTO films (title, description, release_date, duration, mpa_id)
SELECT 'film1', 'description1', '2000-01-01', 100, 1
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'film1');

INSERT INTO films (title, description, release_date, duration, mpa_id)
SELECT 'film2', 'description2', '2000-02-02', 100, 2
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'film2');

INSERT INTO films (title, description, release_date, duration, mpa_id)
SELECT 'film3', 'description3', '2000-03-03', 100, 3
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'film3');

INSERT INTO films (title, description, release_date, duration, mpa_id)
SELECT 'film4', 'description1', '2000-04-04', 100, 4
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'film4');

INSERT INTO films (title, description, release_date, duration, mpa_id)
SELECT 'film5', 'description5', '2000-05-05', 100, 5
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'film5');