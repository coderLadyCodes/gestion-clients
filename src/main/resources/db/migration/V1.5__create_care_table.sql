CREATE TABLE IF NOT EXISTS cares (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT,
    user_id BIGINT,
    product_id BIGINT,
    program_id BIGINT,
    care_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    duration_weeks INT NOT NULL,
    created DATE,
    modified DATE,
    FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS care_time_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    care_id BIGINT NOT NULL,
    time_slot VARCHAR(255) NOT NULL,
    FOREIGN KEY (care_id) REFERENCES cares(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS care_days_of_week (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    care_id BIGINT NOT NULL,
    days_of_week VARCHAR(20) NOT NULL,
    FOREIGN KEY (care_id) REFERENCES cares(id) ON DELETE CASCADE
);
