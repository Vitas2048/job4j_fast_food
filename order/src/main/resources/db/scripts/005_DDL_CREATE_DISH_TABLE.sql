CREATE TABLE dish (
    id serial primary key not null,
    name text unique,
    price int
);