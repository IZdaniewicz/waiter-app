CREATE TABLE IF NOT EXISTS permissions (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS role_permission (
                                 role_id INT REFERENCES roles(id),
                                 permission_id INT REFERENCES permissions(id),
                                 PRIMARY KEY (role_id, permission_id)
);
