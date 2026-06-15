Alter table outbox_events
Add column IF NOT EXISTS attempts_number int not null default 0;