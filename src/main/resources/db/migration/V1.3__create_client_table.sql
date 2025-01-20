CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    comments LONGTEXT,
    email VARCHAR(255) UNIQUE,
    mobile_phone VARCHAR(50),
    home_phone VARCHAR(50),
    birthday DATE,
    city VARCHAR(255),
    street_name VARCHAR(255),
    zip_code VARCHAR(20),
    created DATE,
    modified DATE,
    sex VARCHAR(50),
    user_id BIGINT,
    CONSTRAINT idx_last_name_first_name UNIQUE (last_name, first_name),
    CONSTRAINT idx_zip_code UNIQUE (zip_code)
);
