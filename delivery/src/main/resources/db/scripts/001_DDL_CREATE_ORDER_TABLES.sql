CREATE TABLE ff_order (
    id serial primary key not null,
    total_sum int,
    customer_id int,
    status_id int references status(id)
);

