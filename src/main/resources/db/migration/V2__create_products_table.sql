-- V2__create_products_table.sql
CREATE TABLE products (
                          id              BIGSERIAL       PRIMARY KEY,
                          name            VARCHAR(200)    NOT NULL,
                          price           NUMERIC(10,2)   NOT NULL CHECK (price >= 0),
                          stock_quantity  INT             NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
                          category        VARCHAR(100)    NOT NULL
);