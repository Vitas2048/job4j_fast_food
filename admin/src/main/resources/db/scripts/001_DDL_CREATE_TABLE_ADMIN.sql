CREATE TABLE IF NOT EXISTS food_admin(
    id serial primary key not null,
    login text unique,
    password text
);