CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role_id INTEGER,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS users_basic_info (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255),
                                  surname VARCHAR(255),
                                  user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS token (
                                     id SERIAL PRIMARY KEY,
                                     token VARCHAR(255) UNIQUE NOT NULL,
                                     tokenType VARCHAR(20) DEFAULT 'BEARER',
                                     revoked BOOLEAN NOT NULL,
                                     expired BOOLEAN NOT NULL,
                                     user_id INTEGER,
                                     FOREIGN KEY (user_id) REFERENCES users(id)
);
