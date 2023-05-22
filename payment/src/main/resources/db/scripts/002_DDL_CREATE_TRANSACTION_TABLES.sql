CREATE TABLE IF NOT EXISTS food_transaction (
    id serial primary key not null,
    customer_id int,
    card_discount int,
    order_id int
);

