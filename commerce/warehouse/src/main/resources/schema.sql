CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS dimension (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    width DOUBLE PRECISION NOT NULL CHECK (width >= 1),
    height DOUBLE PRECISION NOT NULL CHECK (height >= 1),
    depth DOUBLE PRECISION NOT NULL CHECK (depth >= 1)
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    house VARCHAR(50),
    flat VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS warehouse_product (
   product_id UUID NOT NULL UNIQUE,
   quantity BIGINT NOT NULL CHECK (quantity >= 0),
   weight DOUBLE PRECISION NOT NULL CHECK (weight >= 1),
   fragile BOOLEAN,
   dimension_id UUID NOT NULL UNIQUE REFERENCES dimension(id)
);
