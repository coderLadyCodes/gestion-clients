CREATE TABLE IF NOT EXISTS jwt (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    valeur VARCHAR(255) NOT NULL,
    desactive BOOLEAN NOT NULL,
    expire BOOLEAN NOT NULL,
    user_id BIGINT,
    refresh_token_id BIGINT,
    CONSTRAINT fk_jwt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_jwt_refresh_token FOREIGN KEY (refresh_token_id) REFERENCES refresh_tokens (id) ON DELETE CASCADE
);
