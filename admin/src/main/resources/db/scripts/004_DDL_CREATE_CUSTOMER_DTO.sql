CREATE TABLE IF NOT EXISTS customer_dto (
    id serial primary key not null,
    login text unique
);

