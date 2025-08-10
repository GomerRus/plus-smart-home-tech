CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS shopping_carts (
    cart_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    user_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID REFERENCES shopping_carts(cart_id),
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (cart_id, product_id)
);



