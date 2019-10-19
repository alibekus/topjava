DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 3, 5, 8, 0, 0), 'Завтрак', 850);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 3, 5, 13, 30, 0), 'Обед', 1230);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 3, 5, 19, 30, 0), 'Ужин', 940);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 3, 5, 7, 30, 0), 'Завтрак', 740);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 3, 5, 12, 30, 0), 'Обед', 1160);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 3, 5, 18, 45, 0), 'Ужин', 670);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 4, 10, 7, 40, 0), 'Завтрак', 630);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 4, 10, 13, 10, 0), 'Обед', 1530);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, make_timestamp(2019, 4, 10, 19, 25, 0), 'Ужин', 375);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 4, 10, 7, 35, 0), 'Завтрак', 475);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 4, 10, 13, 5, 0), 'Обед', 1310);
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, make_timestamp(2019, 4, 10, 19, 45, 0), 'Ужин', 590);