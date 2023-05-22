CREATE TABLE IF NOT EXISTS customer (
    id serial primary key not null,
    login text unique,
    password text
);

