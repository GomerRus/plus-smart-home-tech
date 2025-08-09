CREATE TABLE IF NOT EXISTS dimension (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    width DOUBLE PRECISION NOT NULL CHECK (width >= 1),
    height DOUBLE PRECISION NOT NULL CHECK (height >= 1),
    depth DOUBLE PRECISION NOT NULL CHECK (depth >= 1)
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house VARCHAR(50) NOT NULL,
    flat VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS warehouse_product (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   product_id UUID NOT NULL UNIQUE,
   quantity BIGINT NOT NULL CHECK (quantity >= 0),
   weight DOUBLE PRECISION NOT NULL CHECK (weight >= 1),
   fragile BOOLEAN NOT NULL,
   dimension_id UUID NOT NULL UNIQUE REFERENCES dimension(id),
   address_id UUID NOT NULL REFERENCES address(id)
);
