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

INSERT INTO meals (user_id, date_time, description, calories) VALUES
    (100000, '2019-3-5 08:00:00', 'Завтрак', 850),
    (100000, '2019-3-5 13:30:00', 'Обед', 1230),
    (100000, '2019-3-5 19:30:00', 'Ужин', 940),
    (100001, '2019-3-5 07:30:00', 'Завтрак', 740),
    (100001, '2019-3-5 12:30:00', 'Обед', 1160),
    (100001, '2019-3-5 18:45:00', 'Ужин', 670),
    (100000, '2019-4-10 07:40:00', 'Завтрак', 630),
    (100000, '2019-4-10 13:10:00', 'Обед', 1530),
    (100000, '2019-4-10 19:25:00', 'Ужин', 375),
    (100001, '2019-4-10 07:35:00', 'Завтрак', 475),
    (100001, '2019-4-10 13:05:00', 'Обед', 1310),
    (100001, '2019-4-10 19:45:00', 'Ужин', 590);