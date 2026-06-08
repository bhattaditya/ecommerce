CREATE TABLE audit_logs (
                            id           BIGSERIAL       PRIMARY KEY,
                            action       VARCHAR(50)     NOT NULL,
                            performed_by VARCHAR(150)    NOT NULL,
                            details      TEXT,
                            created_at   TIMESTAMP       NOT NULL DEFAULT now()
);