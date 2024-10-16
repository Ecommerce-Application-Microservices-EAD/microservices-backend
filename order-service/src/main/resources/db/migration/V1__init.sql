CREATE TABLE t_orders (
                          id BIGSERIAL PRIMARY KEY,
                          order_number VARCHAR(255) DEFAULT NULL,
                          sku_code VARCHAR(255),
                          price DECIMAL(19, 2),
                          quantity INTEGER
);
