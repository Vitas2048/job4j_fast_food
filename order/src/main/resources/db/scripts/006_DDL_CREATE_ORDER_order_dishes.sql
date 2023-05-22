CREATE TABLE order_dishes (
    order_id int references ff_order(id),
    dish_id int references dish(id)
);

