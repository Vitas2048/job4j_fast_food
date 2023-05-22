CREATE TABLE IF NOT EXISTS customer (
    id serial primary key not null,
    login text unique,
    password text,
    card_id int references card(id)
);

