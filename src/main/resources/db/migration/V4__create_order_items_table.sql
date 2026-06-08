CREATE TABLE order_items (
                             id                  BIGSERIAL       PRIMARY KEY,
                             order_id            BIGINT          NOT NULL REFERENCES orders(id),
                             product_id          BIGINT          NOT NULL REFERENCES products(id),
                             quantity            INT             NOT NULL CHECK (quantity > 0),
                             price_at_purchase   NUMERIC(10,2)   NOT NULL CHECK (price_at_purchase >= 0)
);