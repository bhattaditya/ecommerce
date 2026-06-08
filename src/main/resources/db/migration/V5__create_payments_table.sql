CREATE TABLE payments (
                          id              BIGSERIAL       PRIMARY KEY,
                          order_id        BIGINT          NOT NULL REFERENCES orders(id),
                          amount          NUMERIC(10,2)   NOT NULL CHECK (amount > 0),
                          status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
                          transaction_id  VARCHAR(100),
                          created_at      TIMESTAMP       NOT NULL DEFAULT now()
);