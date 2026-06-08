CREATE TABLE orders (
                        id              BIGSERIAL       PRIMARY KEY,
                        user_id         BIGINT          NOT NULL REFERENCES users(id),
                        order_status    VARCHAR(20)     NOT NULL DEFAULT 'CREATED',
                        total_amount    NUMERIC(10,2)   NOT NULL CHECK (total_amount >= 0),
                        created_at      TIMESTAMP       NOT NULL DEFAULT now()
);