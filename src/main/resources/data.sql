-- Добавляем роли
INSERT INTO roles (role)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

-- Добавляем пользователей (пароли должны быть захешированы!)
INSERT INTO users (username, email, password, first_name, last_name, age)
VALUES ('admin', 'admin@mail.ru', '12345', "alex", "admin", 30),
       ('user', 'user@mail.ru', '123345', "sergey", "user", 35);

-- Назначаем роли
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), -- admin получает ROLE_ADMIN
       (1, 2), -- admin также получает ROLE_USER
       (2, 2); -- user получает ROLE_USER