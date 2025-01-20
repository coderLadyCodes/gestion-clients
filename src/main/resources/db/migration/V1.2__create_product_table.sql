CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    ref_product VARCHAR(255) NOT NULL,
    description LONGTEXT,
    product_price DOUBLE NOT NULL,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_name (name),
    INDEX idx_ref_product (ref_product),
    INDEX idx_type (type)
);
