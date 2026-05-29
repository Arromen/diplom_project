CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

-- Наполняем тестовыми учетными записями (пароль у всех: 123)
INSERT INTO users (username, password, role) VALUES
                                                 ('admin', '{noop}123', 'ROLE_ADMIN'),
                                                 ('teacher', '{noop}123', 'ROLE_TEACHER'),
                                                 ('guest', '{noop}123', 'ROLE_GUEST')
    ON CONFLICT (username) DO NOTHING;