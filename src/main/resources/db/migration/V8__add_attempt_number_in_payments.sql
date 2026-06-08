ALTER TABLE payments
    ADD COLUMN attempt_number INT NOT NULL DEFAULT 1;