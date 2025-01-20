CREATE TABLE IF NOT EXISTS programs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    client_id BIGINT,
    program_reference VARCHAR(255) UNIQUE NOT NULL,
    created_date DATE,
    total_program_price DECIMAL(10, 2),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);
