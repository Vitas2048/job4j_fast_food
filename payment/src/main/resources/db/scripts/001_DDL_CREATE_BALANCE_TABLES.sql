CREATE TABLE IF NOT EXISTS balance (
    id serial primary key not null,
    sum int,
    customer_id int unique
);

