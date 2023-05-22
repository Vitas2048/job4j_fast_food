CREATE TABLE IF NOT EXISTS customer_token (
    id serial primary key not null,
    access_token text,
    refresh_token text,
    customer_id int references customer(id)
);

