CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    product_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image_src VARCHAR(512),
    quantity_state VARCHAR(255) NOT NULL,
    product_state VARCHAR(255) NOT NULL,
    product_category VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL CHECK (price > 0)
);
